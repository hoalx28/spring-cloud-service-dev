package springproject.iam.v1.model.dto.badcredential;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BadCredentialCreation {
  @UUID(message = "accessTokenId must be in uuid pattern")
  @NotBlank(message = "accessTokenId can not be blank")
  String accessTokenId;

  @NotBlank(message = "accessTokenExpiredAt can not be blank")
  Date accessTokenExpiredAt;

  @NotNull(message = "userId can not be null") Long userId;
}
