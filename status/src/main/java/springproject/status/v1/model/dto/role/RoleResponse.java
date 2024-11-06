package springproject.status.v1.model.dto.role;

import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import springproject.status.v1.model.dto.privilege.PrivilegeResponse;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleResponse {
  Long id;
  String name;
  String description;

  Set<PrivilegeResponse> privileges;
}
