package springproject.iam.v1.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.UUID;
import springproject.iam.v1.introspector.BadCredentialIntrospector;

@Data
@EqualsAndHashCode(exclude = {})
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@EntityListeners(value = BadCredentialIntrospector.class)
@Table
public class BadCredential implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @UUID(message = "accessTokenId must be in uuid pattern")
  @NotBlank(message = "accessTokenId can not be blank")
  @Column(name = "access_token_id", nullable = false)
  String accessTokenId;

  @Temporal(TemporalType.TIMESTAMP)
  @NotNull(message = "accessTokenExpiredAt can not be blank") @Column(name = "access_token_expired_at", nullable = false)
  Date accessTokenExpiredAt;

  @NotNull(message = "userId can not be blank") @Column(name = "user_id", nullable = false)
  Long userId;
}
