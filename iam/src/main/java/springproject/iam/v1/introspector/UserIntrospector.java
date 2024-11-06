package springproject.iam.v1.introspector;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import springproject.iam.v1.model.User;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserIntrospector {
  PasswordEncoder passwordEncoder;

  @PrePersist
  @PreUpdate
  void encryptPassword(User user) {
    Pattern bcryptPattern = Pattern.compile("\\A\\$2a?\\$\\d\\d\\$[./0-9A-Za-z]{53}");
    boolean isEncryptPassword = bcryptPattern.matcher(user.getPassword()).matches();
    if (!isEncryptPassword) {
      user.setPassword(passwordEncoder.encode(user.getPassword()));
    }
  }
}
