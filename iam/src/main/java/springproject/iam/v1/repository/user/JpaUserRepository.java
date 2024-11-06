package springproject.iam.v1.repository.user;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import springproject.iam.v1.model.User;

@Repository
public interface JpaUserRepository extends JpaRepository<User, Long> {
  @Query(
      value =
          "SELECT COUNT(U.id) FROM User AS U WHERE U.username = :username AND deleted IN (TRUE, FALSE)",
      nativeQuery = true)
  long countExistedByUsername(@Param("username") String username);

  @Query(value = "SELECT * FROM User WHERE deleted = TRUE", nativeQuery = true)
  Page<User> findAllByDeletedTrue(Pageable pageable);

  Page<User> findAllByUsernameContains(String username, Pageable pageable);

  Optional<User> findByUsername(String username);
}
