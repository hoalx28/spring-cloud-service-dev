package springproject.iam.v1.service.user;

import springproject.iam.v1.model.dto.user.UserCreation;
import springproject.iam.v1.model.dto.user.UserResponse;
import springproject.iam.v1.model.dto.user.UserUpdate;
import springproject.iam.v1.response.MultiResource;

public interface AbstractUserService {
  void ensureNotExistedByUsername(String username);

  UserResponse save(UserCreation creation);

  UserResponse findById(Long id);

  UserResponse findByUsername(String username);

  MultiResource<UserResponse> findAll(int page, int size);

  MultiResource<UserResponse> findAllByDeletedTrue(int page, int size);

  MultiResource<UserResponse> findAllByUsernameContains(String username, int page, int size);

  UserResponse updateById(Long id, UserUpdate update);

  UserResponse deleteById(Long id);
}
