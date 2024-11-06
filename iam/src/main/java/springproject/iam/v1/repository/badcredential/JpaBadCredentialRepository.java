package springproject.iam.v1.repository.badcredential;

import org.springframework.data.jpa.repository.JpaRepository;
import springproject.iam.v1.model.BadCredential;

public interface JpaBadCredentialRepository extends JpaRepository<BadCredential, Long> {

  boolean existsByAccessTokenId(String id);
}
