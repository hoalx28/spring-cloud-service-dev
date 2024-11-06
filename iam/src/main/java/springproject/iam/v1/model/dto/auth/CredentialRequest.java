package springproject.iam.v1.model.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CredentialRequest {

  @NotBlank(message = "username can not be blank")
  String username;

  @NotBlank(message = "password can not be blank")
  String password;
}
