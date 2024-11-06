package springproject.iam.v1.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.NumericBooleanConverter;
import springproject.iam.v1.introspector.RoleIntrospector;

@Data
@EqualsAndHashCode(exclude = {"users"})
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@EntityListeners(value = RoleIntrospector.class)
@Table
@SoftDelete
public class Role implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @NotBlank(message = "name can not be blank")
  @Column(name = "name", unique = true, nullable = false)
  String name;

  @NotBlank(message = "description can not be blank")
  @Column(name = "description", nullable = false)
  String description;

  @CreationTimestamp LocalDateTime created;
  @UpdateTimestamp LocalDateTime updated;

  @ManyToMany(mappedBy = "roles")
  Set<User> users;

  @ManyToMany
  @SoftDelete(strategy = SoftDeleteType.ACTIVE, converter = NumericBooleanConverter.class)
  Set<Privilege> privileges;
}
