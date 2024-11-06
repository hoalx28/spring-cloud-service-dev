package springproject.iam.v1.model.dto.auth;

import jakarta.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequest {
  @NotBlank(message = "username can not be blank")
  String username;

  @NotBlank(message = "password can not be blank")
  String password;

  Set<Long> roleIds = new HashSet<>(List.of(1L));
}
