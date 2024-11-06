package springproject.iam.v1.mapper.struct;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import springproject.iam.v1.model.User;
import springproject.iam.v1.model.dto.user.UserCreation;
import springproject.iam.v1.model.dto.user.UserResponse;
import springproject.iam.v1.model.dto.user.UserUpdate;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
  User asModel(UserCreation creation);

  UserResponse asResponse(User model);

  List<UserResponse> asCollectionResponse(List<User> models);

  void mergeModel(@MappingTarget User model, UserUpdate update);
}
