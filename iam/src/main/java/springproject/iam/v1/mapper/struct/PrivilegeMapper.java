package springproject.iam.v1.mapper.struct;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import springproject.iam.v1.model.Privilege;
import springproject.iam.v1.model.dto.privilege.PrivilegeCreation;
import springproject.iam.v1.model.dto.privilege.PrivilegeResponse;
import springproject.iam.v1.model.dto.privilege.PrivilegeUpdate;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PrivilegeMapper {
  Privilege asModel(PrivilegeCreation creation);

  PrivilegeResponse asResponse(Privilege model);

  List<PrivilegeResponse> asCollectionResponse(List<Privilege> models);

  void mergeModel(@MappingTarget Privilege model, PrivilegeUpdate update);
}
