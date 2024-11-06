package springproject.iam.v1.service.role;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import springproject.iam.v1.constant.Failed;
import springproject.iam.v1.exception.ServiceException;
import springproject.iam.v1.mapper.struct.RoleMapper;
import springproject.iam.v1.model.Privilege;
import springproject.iam.v1.model.Role;
import springproject.iam.v1.model.dto.role.RoleCreation;
import springproject.iam.v1.model.dto.role.RoleResponse;
import springproject.iam.v1.model.dto.role.RoleUpdate;
import springproject.iam.v1.repository.privilege.JpaPrivilegeRepository;
import springproject.iam.v1.repository.role.JpaRoleRepository;
import springproject.iam.v1.response.MultiResource;
import springproject.iam.v1.response.Paging;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JpaRoleService implements AbstractRoleService {
  JpaRoleRepository jpaRoleRepository;
  RoleMapper roleMapper;
  JpaPrivilegeRepository jpaPrivilegeRepository;

  @Override
  public void ensureNotExistedByName(String name) {
    long existedRecord = jpaRoleRepository.countExistedByName(name);
    if (existedRecord > 0) {
      throw new ServiceException(Failed.ALREADY_EXISTED);
    }
  }

  @Override
  public RoleResponse save(RoleCreation creation) {
    try {
      this.ensureNotExistedByName(creation.getName());
      Role model = roleMapper.asModel(creation);
      List<Privilege> owning = jpaPrivilegeRepository.findAllById(creation.getPrivilegeIds());
      if (CollectionUtils.isEmpty(owning)) {
        throw new ServiceException(Failed.OWNING_SIDE_NOT_EXISTS);
      }
      model.setPrivileges(new HashSet<>(owning));
      Role saved = jpaRoleRepository.save(model);
      return roleMapper.asResponse(saved);
    } catch (ServiceException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServiceException(Failed.SAVE);
    }
  }

  @Override
  public RoleResponse findById(Long id) {
    try {
      Optional<Role> queried = jpaRoleRepository.findById(id);
      if (!queried.isPresent()) {
        throw new ServiceException(Failed.FIND_BY_ID_NO_CONTENT);
      }
      RoleResponse response = roleMapper.asResponse(queried.get());
      return response;
    } catch (ServiceException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServiceException(Failed.FIND_BY_ID);
    }
  }

  @Override
  public MultiResource<RoleResponse> findAll(int page, int size) {
    try {
      Page<Role> pageable = jpaRoleRepository.findAll(PageRequest.of(page, size));
      List<RoleResponse> values = roleMapper.asCollectionResponse(pageable.getContent());
      if (CollectionUtils.isEmpty(values)) {
        throw new ServiceException(Failed.FIND_ALL_NO_CONTENT);
      }
      Paging paging = new Paging(page, pageable.getTotalPages(), pageable.getTotalElements());
      return new MultiResource<>(values, paging);
    } catch (ServiceException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServiceException(Failed.FIND_ALL);
    }
  }

  @Override
  public MultiResource<RoleResponse> findAllByDeletedTrue(int page, int size) {
    try {
      Page<Role> pageable = jpaRoleRepository.findAllByDeletedTrue(PageRequest.of(page, size));
      List<RoleResponse> values = roleMapper.asCollectionResponse(pageable.getContent());
      if (CollectionUtils.isEmpty(values)) {
        throw new ServiceException(Failed.FIND_ALL_NO_CONTENT);
      }
      Paging paging = new Paging(page, pageable.getTotalPages(), pageable.getTotalElements());
      return new MultiResource<>(values, paging);
    } catch (ServiceException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServiceException(Failed.FIND_ALL);
    }
  }

  @Override
  public MultiResource<RoleResponse> findAllByNameContains(String name, int page, int size) {
    try {
      Page<Role> pageable =
          jpaRoleRepository.findAllByNameContains(name, PageRequest.of(page, size));
      List<RoleResponse> values = roleMapper.asCollectionResponse(pageable.getContent());
      if (CollectionUtils.isEmpty(values)) {
        throw new ServiceException(Failed.FIND_ALL_BY_NO_CONTENT);
      }
      Paging paging = new Paging(page, pageable.getTotalPages(), pageable.getTotalElements());
      return new MultiResource<>(values, paging);
    } catch (ServiceException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServiceException(Failed.FIND_ALL_BY);
    }
  }

  @Override
  public RoleResponse updateById(Long id, RoleUpdate update) {
    try {
      Optional<Role> old = jpaRoleRepository.findById(id);
      if (!old.isPresent()) {
        throw new ServiceException(Failed.NOT_EXISTS);
      }
      Role value = old.get();
      roleMapper.mergeModel(value, update);
      Role model = jpaRoleRepository.save(value);
      RoleResponse response = roleMapper.asResponse(model);
      return response;
    } catch (ServiceException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServiceException(Failed.UPDATE);
    }
  }

  @Override
  public RoleResponse deleteById(Long id) {
    try {
      Optional<Role> old = jpaRoleRepository.findById(id);
      if (!old.isPresent()) {
        throw new ServiceException(Failed.NOT_EXISTS);
      }
      Role value = old.get();
      RoleResponse response = roleMapper.asResponse(value);
      jpaRoleRepository.delete(value);
      return response;
    } catch (ServiceException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServiceException(Failed.DELETE);
    }
  }
}
