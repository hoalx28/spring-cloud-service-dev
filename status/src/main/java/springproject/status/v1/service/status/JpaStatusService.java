package springproject.status.v1.service.status;

import feign.FeignException;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import springproject.status.v1.constant.Failed;
import springproject.status.v1.exception.ServiceException;
import springproject.status.v1.mapper.struct.StatusMapper;
import springproject.status.v1.model.Status;
import springproject.status.v1.model.dto.status.StatusCreation;
import springproject.status.v1.model.dto.status.StatusResponse;
import springproject.status.v1.model.dto.status.StatusUpdate;
import springproject.status.v1.model.dto.user.UserResponse;
import springproject.status.v1.repository.status.JpaStatusRepository;
import springproject.status.v1.response.MultiResource;
import springproject.status.v1.response.Paging;
import springproject.status.v1.response.Response;
import springproject.status.v1.service.user.UserServiceClient;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JpaStatusService implements AbstractStatusService {
  JpaStatusRepository jpaStatusRepository;
  StatusMapper statusMapper;
  UserServiceClient userServiceClient;

  @Override
  public void ensureNotExistedByContent(String content) {
    long existedRecord = jpaStatusRepository.countExistedByContent(content);
    if (existedRecord > 0) {
      throw new ServiceException(Failed.ALREADY_EXISTED);
    }
  }

  @PreAuthorize("hasAnyAuthority('CREATE')")
  @Override
  public StatusResponse save(StatusCreation creation) {
    try {
      this.ensureNotExistedByContent(creation.getContent());
      ResponseEntity<Response<UserResponse>> owningQueriedResponse =
          userServiceClient.findById(creation.getUserId());
      HttpStatusCode userQueriedStatusCode = owningQueriedResponse.getStatusCode();
      if (userQueriedStatusCode.isSameCodeAs(HttpStatus.NO_CONTENT)) {
        throw new ServiceException(Failed.OWNING_SIDE_NOT_EXISTS);
      }
      Status model = statusMapper.asModel(creation);
      Status saved = jpaStatusRepository.save(model);
      return statusMapper.asResponse(saved);
    } catch (ServiceException | FeignException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServiceException(Failed.SAVE);
    }
  }

  @PreAuthorize("hasAnyAuthority('READ')")
  @Override
  public StatusResponse findById(Long id) {
    try {
      Optional<Status> queried = jpaStatusRepository.findById(id);
      if (!queried.isPresent()) {
        throw new ServiceException(Failed.FIND_BY_ID_NO_CONTENT);
      }
      ResponseEntity<Response<UserResponse>> owningQueriedResponse =
          userServiceClient.findById(queried.get().getUserId());
      HttpStatusCode userQueriedStatusCode = owningQueriedResponse.getStatusCode();
      if (userQueriedStatusCode.isSameCodeAs(HttpStatus.NO_CONTENT)) {
        throw new ServiceException(Failed.OWNING_SIDE_NOT_EXISTS);
      }
      StatusResponse response = statusMapper.asResponse(queried.get());
      UserResponse owning = owningQueriedResponse.getBody().getPayload();
      response.setUser(owning);
      return response;
    } catch (ServiceException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServiceException(Failed.FIND_BY_ID);
    }
  }

  @PreAuthorize("hasAnyAuthority('READ')")
  @Override
  public MultiResource<StatusResponse> findAll(int page, int size) {
    try {
      Page<Status> pageable = jpaStatusRepository.findAll(PageRequest.of(page, size));
      List<StatusResponse> values = statusMapper.asCollectionResponse(pageable.getContent());
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

  @PreAuthorize("hasAnyAuthority('READ')")
  @Override
  public MultiResource<StatusResponse> findAllByDeletedTrue(int page, int size) {
    try {
      Page<Status> pageable = jpaStatusRepository.findAllByDeletedTrue(PageRequest.of(page, size));
      List<StatusResponse> values = statusMapper.asCollectionResponse(pageable.getContent());
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

  @PreAuthorize("hasAnyAuthority('READ')")
  @Override
  public MultiResource<StatusResponse> findAllByContentContains(
      String content, int page, int size) {
    try {
      Page<Status> pageable =
          jpaStatusRepository.findAllByContentContains(content, PageRequest.of(page, size));
      List<StatusResponse> values = statusMapper.asCollectionResponse(pageable.getContent());
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

  @PreAuthorize("hasAnyAuthority('UPDATE')")
  @Override
  public StatusResponse updateById(Long id, StatusUpdate update) {
    try {
      Optional<Status> old = jpaStatusRepository.findById(id);
      if (!old.isPresent()) {
        throw new ServiceException(Failed.NOT_EXISTS);
      }
      Status value = old.get();
      String oldIdentity = value.getContent();
      statusMapper.mergeModel(value, update);
      if (!oldIdentity.equals(value.getContent())) {
        this.ensureNotExistedByContent(value.getContent());
      }
      Status model = jpaStatusRepository.save(value);
      StatusResponse response = statusMapper.asResponse(model);
      return response;
    } catch (ServiceException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServiceException(Failed.UPDATE);
    }
  }

  @PreAuthorize("hasAnyAuthority('DELETE')")
  @Override
  public StatusResponse deleteById(Long id) {
    try {
      Optional<Status> old = jpaStatusRepository.findById(id);
      if (!old.isPresent()) {
        throw new ServiceException(Failed.NOT_EXISTS);
      }
      Status value = old.get();
      StatusResponse response = statusMapper.asResponse(value);
      jpaStatusRepository.delete(value);
      return response;
    } catch (ServiceException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServiceException(Failed.DELETE);
    }
  }
}
