package springproject.iam.v1.mapper.struct;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import springproject.iam.v1.model.Role;
import springproject.iam.v1.model.dto.role.RoleCreation;
import springproject.iam.v1.model.dto.role.RoleResponse;
import springproject.iam.v1.model.dto.role.RoleUpdate;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RoleMapper {
  Role asModel(RoleCreation creation);

  RoleResponse asResponse(Role model);

  List<RoleResponse> asCollectionResponse(List<Role> models);

  void mergeModel(@MappingTarget Role model, RoleUpdate update);
}
