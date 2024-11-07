package springproject.status.v1.model.dto.status;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import springproject.status.v1.model.dto.user.UserResponse;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatusResponse {
  Long id;
  String content;

  UserResponse user;
}
