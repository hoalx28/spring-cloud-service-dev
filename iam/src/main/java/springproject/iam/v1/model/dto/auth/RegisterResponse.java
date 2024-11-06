package springproject.iam.v1.model.dto.auth;

import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import springproject.iam.v1.model.dto.role.RoleResponse;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterResponse {
  Long id;
  String username;

  Set<RoleResponse> roles;
}
