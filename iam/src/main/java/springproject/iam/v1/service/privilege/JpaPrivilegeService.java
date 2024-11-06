package springproject.iam.v1.service.privilege;

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
import springproject.iam.v1.mapper.struct.PrivilegeMapper;
import springproject.iam.v1.model.Privilege;
import springproject.iam.v1.model.dto.privilege.PrivilegeCreation;
import springproject.iam.v1.model.dto.privilege.PrivilegeResponse;
import springproject.iam.v1.model.dto.privilege.PrivilegeUpdate;
import springproject.iam.v1.repository.privilege.JpaPrivilegeRepository;
import springproject.iam.v1.response.MultiResource;
import springproject.iam.v1.response.Paging;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JpaPrivilegeService implements AbstractPrivilegeService {
  JpaPrivilegeRepository jpaPrivilegeRepository;
  PrivilegeMapper privilegeMapper;

  @Override
  public void ensureNotExistedByName(String name) {
    long existedRecord = jpaPrivilegeRepository.countExistedByName(name);
    if (existedRecord > 0) {
      throw new ServiceException(Failed.ALREADY_EXISTED);
    }
  }

  @Override
  public PrivilegeResponse save(PrivilegeCreation creation) {
    try {
      this.ensureNotExistedByName(creation.getName());
      Privilege model = privilegeMapper.asModel(creation);
      Privilege saved = jpaPrivilegeRepository.save(model);
      return privilegeMapper.asResponse(saved);
    } catch (ServiceException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServiceException(Failed.SAVE);
    }
  }

  @Override
  public PrivilegeResponse findById(Long id) {
    try {
      Optional<Privilege> queried = jpaPrivilegeRepository.findById(id);
      if (!queried.isPresent()) {
        throw new ServiceException(Failed.FIND_BY_ID_NO_CONTENT);
      }
      PrivilegeResponse response = privilegeMapper.asResponse(queried.get());
      return response;
    } catch (ServiceException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServiceException(Failed.FIND_BY_ID);
    }
  }

  @Override
  public MultiResource<PrivilegeResponse> findAll(int page, int size) {
    try {
      Page<Privilege> pageable = jpaPrivilegeRepository.findAll(PageRequest.of(page, size));
      List<PrivilegeResponse> values = privilegeMapper.asCollectionResponse(pageable.getContent());
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
  public MultiResource<PrivilegeResponse> findAllByDeletedTrue(int page, int size) {
    try {
      Page<Privilege> pageable =
          jpaPrivilegeRepository.findAllByDeletedTrue(PageRequest.of(page, size));
      List<PrivilegeResponse> values = privilegeMapper.asCollectionResponse(pageable.getContent());
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
  public MultiResource<PrivilegeResponse> findAllByNameContains(String name, int page, int size) {
    try {
      Page<Privilege> pageable =
          jpaPrivilegeRepository.findAllByNameContains(name, PageRequest.of(page, size));
      List<PrivilegeResponse> values = privilegeMapper.asCollectionResponse(pageable.getContent());
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
  public PrivilegeResponse updateById(Long id, PrivilegeUpdate update) {
    try {
      Optional<Privilege> old = jpaPrivilegeRepository.findById(id);
      if (!old.isPresent()) {
        throw new ServiceException(Failed.NOT_EXISTS);
      }
      Privilege value = old.get();
      privilegeMapper.mergeModel(value, update);
      Privilege model = jpaPrivilegeRepository.save(value);
      PrivilegeResponse response = privilegeMapper.asResponse(model);
      return response;
    } catch (ServiceException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServiceException(Failed.UPDATE);
    }
  }

  @Override
  public PrivilegeResponse deleteById(Long id) {
    try {
      Optional<Privilege> old = jpaPrivilegeRepository.findById(id);
      if (!old.isPresent()) {
        throw new ServiceException(Failed.NOT_EXISTS);
      }
      Privilege value = old.get();
      PrivilegeResponse response = privilegeMapper.asResponse(value);
      jpaPrivilegeRepository.deleteById(id);
      return response;
    } catch (ServiceException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServiceException(Failed.DELETE);
    }
  }
}
