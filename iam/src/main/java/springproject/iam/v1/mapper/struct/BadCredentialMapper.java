package springproject.iam.v1.mapper.struct;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import springproject.iam.v1.model.BadCredential;
import springproject.iam.v1.model.dto.badcredential.BadCredentialCreation;
import springproject.iam.v1.model.dto.badcredential.BadCredentialResponse;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BadCredentialMapper {
  BadCredential asModel(BadCredentialCreation creation);

  BadCredentialResponse asResponse(BadCredential model);
}
