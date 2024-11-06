package springproject.status.v1.token;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import springproject.status.v1.constant.Failed;
import springproject.status.v1.exception.ServiceException;
import springproject.status.v1.model.dto.user.UserResponse;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NimbusJWTAuthProvider implements AbstractJWTAuthProvider<SignedJWT, UserResponse> {
  @Override
  public String buildScope(UserResponse user) {
    StringJoiner stringJoiner = new StringJoiner(" ");
    if (!CollectionUtils.isEmpty(user.getRoles()))
      user.getRoles()
          .forEach(
              role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPrivileges()))
                  role.getPrivileges()
                      .forEach(permission -> stringJoiner.add(permission.getName()));
              });
    return stringJoiner.toString();
  }

  @Override
  public String sign(
      UserResponse user, long expiredTime, String secretKey, String id, String referId) {
    JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
    JWTClaimsSet jwtClaimsSet =
        new JWTClaimsSet.Builder()
            .subject(user.getUsername())
            .issueTime(new Date())
            .expirationTime(
                new Date(Instant.now().plus(expiredTime, ChronoUnit.SECONDS).toEpochMilli()))
            .jwtID(id)
            .claim("scope", buildScope(user))
            .claim("userId", user.getId())
            .claim("referId", referId)
            .build();
    Payload payload = new Payload(jwtClaimsSet.toJSONObject());
    JWSObject jwsObject = new JWSObject(header, payload);
    try {
      jwsObject.sign(new MACSigner(secretKey.getBytes()));
      return jwsObject.serialize();
    } catch (JOSEException e) {
      e.printStackTrace();
      throw new ServiceException(Failed.SIGN_JWT_TOKEN);
    }
  }

  @Override
  public SignedJWT verify(String token, String secretKey) {
    try {
      JWSVerifier verifier = new MACVerifier(secretKey.getBytes());
      SignedJWT signedJWT = SignedJWT.parse(token);
      boolean isVerify = signedJWT.verify(verifier);
      if (!isVerify) {
        throw new ServiceException(Failed.ILL_LEGAL_JWT_TOKEN);
      }
      Date expiredTime = signedJWT.getJWTClaimsSet().getExpirationTime();
      if (expiredTime.before(new Date())) {
        throw new ServiceException(Failed.JWT_TOKEN_EXPIRED);
      }
      return signedJWT;
    } catch (ServiceException e) {
      throw e;
    } catch (ParseException e) {
      e.printStackTrace();
      throw new ServiceException(Failed.PARSE_JWT_TOKEN);
    } catch (JOSEException e) {
      e.printStackTrace();
      throw new ServiceException(Failed.ILL_LEGAL_JWT_TOKEN);
    }
  }
}
