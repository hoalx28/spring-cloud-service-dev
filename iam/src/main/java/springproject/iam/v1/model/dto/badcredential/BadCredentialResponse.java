package springproject.iam.v1.model.dto.badcredential;

import java.util.Date;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BadCredentialResponse {
  Long id;
  String accessTokenId;
  Date accessTokenExpiredAt;
}
