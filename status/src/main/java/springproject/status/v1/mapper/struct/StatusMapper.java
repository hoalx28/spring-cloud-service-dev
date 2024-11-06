package springproject.status.v1.mapper.struct;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import springproject.status.v1.model.Status;
import springproject.status.v1.model.dto.status.StatusCreation;
import springproject.status.v1.model.dto.status.StatusResponse;
import springproject.status.v1.model.dto.status.StatusUpdate;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StatusMapper {
  Status asModel(StatusCreation creation);

  StatusResponse asResponse(Status model);

  List<StatusResponse> asCollectionResponse(List<Status> models);

  void mergeModel(@MappingTarget Status model, StatusUpdate update);
}
