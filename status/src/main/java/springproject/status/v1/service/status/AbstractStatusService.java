package springproject.status.v1.service.status;

import springproject.status.v1.model.dto.status.StatusCreation;
import springproject.status.v1.model.dto.status.StatusResponse;
import springproject.status.v1.model.dto.status.StatusUpdate;
import springproject.status.v1.response.MultiResource;

public interface AbstractStatusService {
  void ensureNotExistedByContent(String content);

  StatusResponse save(StatusCreation creation);

  StatusResponse findById(Long id);

  MultiResource<StatusResponse> findAll(int page, int size);

  MultiResource<StatusResponse> findAllByDeletedTrue(int page, int size);

  MultiResource<StatusResponse> findAllByContentContains(String content, int page, int size);

  StatusResponse updateById(Long id, StatusUpdate update);

  StatusResponse deleteById(Long id);
}
