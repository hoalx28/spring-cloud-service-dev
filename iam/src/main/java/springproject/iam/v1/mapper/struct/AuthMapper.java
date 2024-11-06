package springproject.iam.v1.mapper.struct;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import springproject.iam.v1.model.dto.auth.CredentialRequest;
import springproject.iam.v1.model.dto.auth.RegisterRequest;
import springproject.iam.v1.model.dto.auth.RegisterResponse;
import springproject.iam.v1.model.dto.user.UserCreation;
import springproject.iam.v1.model.dto.user.UserResponse;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AuthMapper {
  UserCreation asUserCreation(RegisterRequest request);

  UserCreation asUserCreation(CredentialRequest request);

  RegisterResponse asRegisterResponse(UserResponse response);
}
