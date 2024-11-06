package springproject.iam.v1.service.privilege;

import springproject.iam.v1.model.dto.privilege.PrivilegeCreation;
import springproject.iam.v1.model.dto.privilege.PrivilegeResponse;
import springproject.iam.v1.model.dto.privilege.PrivilegeUpdate;
import springproject.iam.v1.response.MultiResource;

public interface AbstractPrivilegeService {
  void ensureNotExistedByName(String name);

  PrivilegeResponse save(PrivilegeCreation creation);

  PrivilegeResponse findById(Long id);

  MultiResource<PrivilegeResponse> findAll(int page, int size);

  MultiResource<PrivilegeResponse> findAllByDeletedTrue(int page, int size);

  MultiResource<PrivilegeResponse> findAllByNameContains(String name, int page, int size);

  PrivilegeResponse updateById(Long id, PrivilegeUpdate update);

  PrivilegeResponse deleteById(Long id);
}
