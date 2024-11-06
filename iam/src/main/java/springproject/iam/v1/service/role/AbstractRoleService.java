package springproject.iam.v1.service.role;

import springproject.iam.v1.model.dto.role.RoleCreation;
import springproject.iam.v1.model.dto.role.RoleResponse;
import springproject.iam.v1.model.dto.role.RoleUpdate;
import springproject.iam.v1.response.MultiResource;

public interface AbstractRoleService {
  void ensureNotExistedByName(String name);

  RoleResponse save(RoleCreation creation);

  RoleResponse findById(Long id);

  MultiResource<RoleResponse> findAll(int page, int size);

  MultiResource<RoleResponse> findAllByDeletedTrue(int page, int size);

  MultiResource<RoleResponse> findAllByNameContains(String name, int page, int size);

  RoleResponse updateById(Long id, RoleUpdate update);

  RoleResponse deleteById(Long id);
}
