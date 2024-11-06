package springproject.iam.v1.service.badcredential;

import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import springproject.iam.v1.constant.Failed;
import springproject.iam.v1.exception.ServiceException;
import springproject.iam.v1.mapper.struct.BadCredentialMapper;
import springproject.iam.v1.model.BadCredential;
import springproject.iam.v1.model.dto.badcredential.BadCredentialCreation;
import springproject.iam.v1.model.dto.badcredential.BadCredentialResponse;
import springproject.iam.v1.model.dto.user.UserResponse;
import springproject.iam.v1.repository.badcredential.JpaBadCredentialRepository;
import springproject.iam.v1.token.AbstractJWTAuthProvider;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JpaBadCredentialService implements AbstractBadCredentialService {
  @NonFinal
  @Value("${jwt.access-token.secret}")
  String accessTokenSecret;

  AbstractJWTAuthProvider<SignedJWT, UserResponse> jwtAuthProvider;
  JpaBadCredentialRepository jpaBadCredentialRepository;
  BadCredentialMapper badCredentialMapper;

  @Override
  public void ensureNotBadCredential(String token) {
    try {
      SignedJWT signedJWT = jwtAuthProvider.verify(token, accessTokenSecret);
      String tokenId = signedJWT.getJWTClaimsSet().getJWTID();
      boolean isBadCredential = jpaBadCredentialRepository.existsByAccessTokenId(tokenId);
      if (isBadCredential) {
        throw new ServiceException(Failed.JWT_TOKEN_BLOCKED);
      }
    } catch (ServiceException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServiceException(Failed.ENSURE_JWT_TOKEN_NOT_BAD_CREDENTIALS);
    }
  }

  @Override
  public BadCredentialResponse save(BadCredentialCreation creation) {
    try {
      BadCredential model = badCredentialMapper.asModel(creation);
      BadCredential saved = jpaBadCredentialRepository.save(model);
      return badCredentialMapper.asResponse(saved);
    } catch (ServiceException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServiceException(Failed.SAVE);
    }
  }
}
