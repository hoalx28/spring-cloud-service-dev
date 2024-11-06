package springproject.iam.v1.service.user;

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
import springproject.iam.v1.mapper.struct.UserMapper;
import springproject.iam.v1.model.Role;
import springproject.iam.v1.model.User;
import springproject.iam.v1.model.dto.user.UserCreation;
import springproject.iam.v1.model.dto.user.UserResponse;
import springproject.iam.v1.model.dto.user.UserUpdate;
import springproject.iam.v1.repository.role.JpaRoleRepository;
import springproject.iam.v1.repository.user.JpaUserRepository;
import springproject.iam.v1.response.MultiResource;
import springproject.iam.v1.response.Paging;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JpaUserService implements AbstractUserService {
  JpaUserRepository jpaUserRepository;
  JpaRoleRepository jpaRoleRepository;
  UserMapper userMapper;

  @Override
  public void ensureNotExistedByUsername(String username) {
    long existedRecord = jpaUserRepository.countExistedByUsername(username);
    if (existedRecord > 0) {
      throw new ServiceException(Failed.ALREADY_EXISTED);
    }
  }

  @Override
  public UserResponse save(UserCreation creation) {
    try {
      this.ensureNotExistedByUsername(creation.getUsername());
      User model = userMapper.asModel(creation);
      List<Role> owning = jpaRoleRepository.findAllById(creation.getRoleIds());
      if (CollectionUtils.isEmpty(owning)) {
        throw new ServiceException(Failed.OWNING_SIDE_NOT_EXISTS);
      }
      model.setRoles(new HashSet<>(owning));
      User saved = jpaUserRepository.save(model);
      return userMapper.asResponse(saved);
    } catch (ServiceException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServiceException(Failed.SAVE);
    }
  }

  @Override
  public UserResponse findById(Long id) {
    try {
      Optional<User> queried = jpaUserRepository.findById(id);
      if (!queried.isPresent()) {
        throw new ServiceException(Failed.FIND_BY_ID_NO_CONTENT);
      }
      UserResponse response = userMapper.asResponse(queried.get());
      return response;
    } catch (ServiceException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServiceException(Failed.FIND_BY_ID);
    }
  }

  @Override
  public UserResponse findByUsername(String username) {
    try {
      Optional<User> queried = jpaUserRepository.findByUsername(username);
      if (!queried.isPresent()) {
        throw new ServiceException(Failed.NOT_EXISTS);
      }
      UserResponse response = userMapper.asResponse(queried.get());
      return response;
    } catch (ServiceException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServiceException(Failed.FIND_BY_USERNAME);
    }
  }

  @Override
  public MultiResource<UserResponse> findAll(int page, int size) {
    try {
      Page<User> pageable = jpaUserRepository.findAll(PageRequest.of(page, size));
      List<UserResponse> values = userMapper.asCollectionResponse(pageable.getContent());
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
  public MultiResource<UserResponse> findAllByDeletedTrue(int page, int size) {
    try {
      Page<User> pageable = jpaUserRepository.findAllByDeletedTrue(PageRequest.of(page, size));
      List<UserResponse> values = userMapper.asCollectionResponse(pageable.getContent());
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
  public MultiResource<UserResponse> findAllByUsernameContains(
      String username, int page, int size) {
    try {
      Page<User> pageable =
          jpaUserRepository.findAllByUsernameContains(username, PageRequest.of(page, size));
      List<UserResponse> values = userMapper.asCollectionResponse(pageable.getContent());
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
  public UserResponse updateById(Long id, UserUpdate update) {
    try {
      Optional<User> old = jpaUserRepository.findById(id);
      if (!old.isPresent()) {
        throw new ServiceException(Failed.NOT_EXISTS);
      }
      User value = old.get();
      userMapper.mergeModel(value, update);
      User model = jpaUserRepository.save(value);
      UserResponse response = userMapper.asResponse(model);
      return response;
    } catch (ServiceException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServiceException(Failed.UPDATE);
    }
  }

  @Override
  public UserResponse deleteById(Long id) {
    try {
      Optional<User> old = jpaUserRepository.findById(id);
      if (!old.isPresent()) {
        throw new ServiceException(Failed.NOT_EXISTS);
      }
      User value = old.get();
      UserResponse response = userMapper.asResponse(value);
      jpaUserRepository.delete(value);
      return response;
    } catch (ServiceException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServiceException(Failed.DELETE);
    }
  }
}
