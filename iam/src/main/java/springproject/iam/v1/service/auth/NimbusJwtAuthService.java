package springproject.iam.v1.service.auth;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import com.nimbusds.jwt.SignedJWT;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import springproject.iam.v1.constant.Failed;
import springproject.iam.v1.exception.ServiceException;
import springproject.iam.v1.mapper.struct.AuthMapper;
import springproject.iam.v1.model.dto.auth.CredentialRequest;
import springproject.iam.v1.model.dto.auth.CredentialResponse;
import springproject.iam.v1.model.dto.auth.RegisterRequest;
import springproject.iam.v1.model.dto.auth.RegisterResponse;
import springproject.iam.v1.model.dto.badcredential.BadCredentialCreation;
import springproject.iam.v1.model.dto.badcredential.BadCredentialResponse;
import springproject.iam.v1.model.dto.user.UserCreation;
import springproject.iam.v1.model.dto.user.UserResponse;
import springproject.iam.v1.service.badcredential.AbstractBadCredentialService;
import springproject.iam.v1.service.user.AbstractUserService;
import springproject.iam.v1.token.AbstractJWTAuthProvider;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NimbusJwtAuthService implements AbstractJwtAuthService {
  @NonFinal
  @Value("${jwt.access-token.secret}")
  String accessTokenSecret;

  @NonFinal
  @Value("${jwt.access-token.time-to-live}")
  long accessTokenTimeToLive;

  @NonFinal
  @Value("${jwt.refresh-token.secret}")
  String refreshTokenSecret;

  @NonFinal
  @Value("${jwt.refresh-token.time-to-live}")
  long refreshTokenTimeToLive;

  AbstractJWTAuthProvider<SignedJWT, UserResponse> jwtAuthProvider;
  AbstractUserService jpaUserService;
  AbstractBadCredentialService jpaBadCredentialService;
  AuthMapper authMapper;
  PasswordEncoder passwordEncoder;

  public CredentialResponse newCredentials(UserResponse user) {
    String accessId = UUID.randomUUID().toString();
    String refreshId = UUID.randomUUID().toString();
    String accessToken = jwtAuthProvider.sign(user, accessTokenTimeToLive, accessTokenSecret, accessId, refreshId);
    long accessTokenIssuedAt = System.currentTimeMillis();
    String refreshToken = jwtAuthProvider.sign(user, refreshTokenTimeToLive, refreshTokenSecret, refreshId, accessId);
    long refreshTokenIssuedAt = System.currentTimeMillis();
    return new CredentialResponse(
        accessToken, accessTokenIssuedAt, refreshToken, refreshTokenIssuedAt);
  }

  public BadCredentialCreation asBadCredentialCreation(String accessToken) throws ParseException {
    SignedJWT accessSignedJWT = jwtAuthProvider.verify(accessToken, accessTokenSecret);
    Long userId = (Long) accessSignedJWT.getJWTClaimsSet().getClaim("userId");
    String accessTokenId = accessSignedJWT.getJWTClaimsSet().getJWTID();
    Date accessTokenExpiredAt = accessSignedJWT.getJWTClaimsSet().getExpirationTime();
    return new BadCredentialCreation(accessTokenId, accessTokenExpiredAt, userId);
  }

  @Override
  public CredentialResponse signUp(RegisterRequest request) {
    try {
      UserCreation creation = authMapper.asUserCreation(request);
      UserResponse user = jpaUserService.save(creation);
      return this.newCredentials(user);
    } catch (ServiceException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServiceException(Failed.SIGN_UP);
    }
  }

  @Override
  public CredentialResponse signIn(CredentialRequest request) {
    try {
      UserResponse user = jpaUserService.findByUsername(request.getUsername());
      boolean isAuthorization = passwordEncoder.matches(request.getPassword(), user.getPassword());
      if (!isAuthorization) {
        throw new ServiceException(Failed.BAD_CREDENTIALS);
      }
      return this.newCredentials(user);
    } catch (ServiceException e) {
      if (Failed.NOT_EXISTS.equals(e.getFailed())) {
        throw new ServiceException(Failed.BAD_CREDENTIALS);
      }
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServiceException(Failed.SIGN_IN);
    }
  }

  @Override
  public boolean identity(String accessToken, String refreshToken) {
    try {
      SignedJWT accessSignedJWT = jwtAuthProvider.verify(accessToken, accessTokenSecret);
      SignedJWT refreshSignedJWT = jwtAuthProvider.verify(refreshToken, refreshTokenSecret);
      String accessReferId = (String) accessSignedJWT.getJWTClaimsSet().getClaim("referId");
      String refreshTokenId = refreshSignedJWT.getJWTClaimsSet().getJWTID();
      if (!refreshTokenId.equals(accessReferId)) {
        throw new ServiceException(Failed.JWT_TOKEN_NOT_SUITABLE);
      }
      jpaBadCredentialService.ensureNotBadCredential(accessToken);
      return true;
    } catch (ServiceException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServiceException(Failed.VERIFY_IDENTITY);
    }
  }

  @Override
  public RegisterResponse me() {
    try {
      String username = SecurityContextHolder.getContext().getAuthentication().getName();
      UserResponse user = jpaUserService.findByUsername(username);
      RegisterResponse response = authMapper.asRegisterResponse(user);
      return response;
    } catch (ServiceException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServiceException(Failed.RETRIEVE_PROFILE);
    }
  }

  @Override
  public long signOut() {
    try {
      String accessToken = ((JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
          .getToken()
          .getTokenValue();
      BadCredentialCreation creation = this.asBadCredentialCreation(accessToken);
      BadCredentialResponse saved = jpaBadCredentialService.save(creation);
      return saved.getId();
    } catch (ServiceException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServiceException(Failed.SIGN_OUT);
    }
  }

  @Override
  public CredentialResponse refresh(String accessToken, String refreshToken) {
    try {
      this.identity(accessToken, refreshToken);
      BadCredentialCreation creation = this.asBadCredentialCreation(accessToken);
      jpaBadCredentialService.save(creation);
      UserResponse user = jpaUserService.findById(creation.getUserId());
      return this.newCredentials(user);
    } catch (ServiceException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServiceException(Failed.REFRESH_JWT_TOKEN);
    }
  }
}
