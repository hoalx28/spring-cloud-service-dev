package springproject.iam.v1.service.auth;

import springproject.iam.v1.model.dto.auth.CredentialRequest;
import springproject.iam.v1.model.dto.auth.CredentialResponse;
import springproject.iam.v1.model.dto.auth.RegisterRequest;
import springproject.iam.v1.model.dto.auth.RegisterResponse;

public interface AbstractJwtAuthService {
  CredentialResponse signUp(RegisterRequest request);

  CredentialResponse signIn(CredentialRequest request);

  boolean identity(String accessToken, String refreshToken);

  RegisterResponse me();

  long signOut();

  CredentialResponse refresh(String accessToken, String refreshToken);
}
