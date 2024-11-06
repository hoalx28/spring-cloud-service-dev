package springproject.status.v1.repository.status;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import springproject.status.v1.model.Status;

@Repository
public interface JpaStatusRepository extends JpaRepository<Status, Long> {
  @Query(
      value =
          "SELECT COUNT(S.id) FROM Status AS S WHERE S.content = :content AND deleted IN (TRUE, FALSE)",
      nativeQuery = true)
  long countExistedByContent(@Param("content") String content);

  @Query(value = "SELECT * FROM Status WHERE deleted = TRUE", nativeQuery = true)
  Page<Status> findAllByDeletedTrue(Pageable pageable);

  Page<Status> findAllByContentContains(String content, Pageable pageable);

  Optional<Status> findByContent(String content);
}
