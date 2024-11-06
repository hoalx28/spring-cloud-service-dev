package springproject.iam.v1.model.dto.auth;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CredentialResponse {
  String accessToken;
  long accessTokenIssuedAt;
  String refreshToken;
  long refreshTokenIssuedAt;
}
