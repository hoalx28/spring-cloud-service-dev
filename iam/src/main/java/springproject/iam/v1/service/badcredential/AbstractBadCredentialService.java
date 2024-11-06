package springproject.iam.v1.service.badcredential;

import springproject.iam.v1.model.dto.badcredential.BadCredentialCreation;
import springproject.iam.v1.model.dto.badcredential.BadCredentialResponse;

public interface AbstractBadCredentialService {
  BadCredentialResponse save(BadCredentialCreation creation);

  void ensureNotBadCredential(String token);
}
