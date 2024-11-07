package springproject.status.v1.controller.individual;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springproject.status.v1.constant.Success;
import springproject.status.v1.model.dto.status.StatusCreation;
import springproject.status.v1.model.dto.status.StatusResponse;
import springproject.status.v1.model.dto.status.StatusUpdate;
import springproject.status.v1.response.MultiResource;
import springproject.status.v1.response.PagingResponse;
import springproject.status.v1.response.Response;
import springproject.status.v1.service.status.AbstractStatusService;

@RestController
@RequestMapping(path = "/api/v1/statuses")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatusController {
  @Qualifier("jpaStatusService") AbstractStatusService jpaStatusService;

  @PostMapping
  public ResponseEntity<Response<Long>> save(@RequestBody @Valid StatusCreation creation) {
    Success created = Success.SAVE;
    StatusResponse saved = jpaStatusService.save(creation);
    Response<Long> response =
        Response.<Long>builder()
            .code(created.getCode())
            .message(created.getMessage())
            .timestamp(System.currentTimeMillis())
            .payload(saved.getId())
            .build();
    return ResponseEntity.status(created.getHttpStatus()).body(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Response<StatusResponse>> findById(
      @PathVariable(name = "id", required = true) Long id) {
    Success ok = Success.FIND_BY_ID;
    StatusResponse queried = jpaStatusService.findById(id);

    Response<StatusResponse> response =
        Response.<StatusResponse>builder()
            .code(ok.getCode())
            .message(ok.getMessage())
            .timestamp(System.currentTimeMillis())
            .payload(queried)
            .build();
    return ResponseEntity.ok().body(response);
  }

  @GetMapping
  public ResponseEntity<PagingResponse<List<StatusResponse>>> findAll(
      @RequestParam(name = "page", defaultValue = "0")
          @Min(value = 0, message = "page index must be start from 0.")
          int page,
      @RequestParam(name = "size", defaultValue = "50")
          @Min(value = 5, message = "page size must be greater than 5.")
          @Max(value = 50, message = "page size must be less than 50.")
          int size) {
    Success ok = Success.FIND_ALL;
    MultiResource<StatusResponse> queried = jpaStatusService.findAll(page, size);

    PagingResponse<List<StatusResponse>> response =
        PagingResponse.<List<StatusResponse>>builder()
            .code(ok.getCode())
            .message(ok.getMessage())
            .timestamp(System.currentTimeMillis())
            .payload(queried.getValues())
            .paging(queried.getPaging())
            .build();
    return ResponseEntity.ok().body(response);
  }

  @GetMapping("/archived")
  public ResponseEntity<PagingResponse<List<StatusResponse>>> findAllByDeletedTrue(
      @RequestParam(name = "page", defaultValue = "0")
          @Min(value = 0, message = "page index must be start from 0.")
          int page,
      @RequestParam(name = "size", defaultValue = "50")
          @Min(value = 5, message = "page size must be greater than 5.")
          @Max(value = 50, message = "page size must be less than 50.")
          int size) {
    Success ok = Success.FIND_ALL;
    MultiResource<StatusResponse> queried = jpaStatusService.findAllByDeletedTrue(page, size);

    PagingResponse<List<StatusResponse>> response =
        PagingResponse.<List<StatusResponse>>builder()
            .code(ok.getCode())
            .message(ok.getMessage())
            .timestamp(System.currentTimeMillis())
            .payload(queried.getValues())
            .paging(queried.getPaging())
            .build();
    return ResponseEntity.ok().body(response);
  }

  @GetMapping("/search")
  public ResponseEntity<PagingResponse<List<StatusResponse>>> findAllByContentContains(
      @RequestParam(name = "content", required = true, defaultValue = "") String content,
      @RequestParam(name = "page", defaultValue = "0")
          @Min(value = 0, message = "page index must be start from 0.")
          int page,
      @RequestParam(name = "size", defaultValue = "50")
          @Min(value = 5, message = "page size must be greater than 5.")
          @Max(value = 50, message = "page size must be less than 50.")
          int size) {
    Success ok = Success.FIND_ALL_BY;
    MultiResource<StatusResponse> queried =
        jpaStatusService.findAllByContentContains(content, page, size);

    PagingResponse<List<StatusResponse>> response =
        PagingResponse.<List<StatusResponse>>builder()
            .code(ok.getCode())
            .message(ok.getMessage())
            .timestamp(System.currentTimeMillis())
            .payload(queried.getValues())
            .paging(queried.getPaging())
            .build();
    return ResponseEntity.ok().body(response);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Response<Long>> updateById(
      @PathVariable(name = "id", required = true) Long id, @RequestBody StatusUpdate update) {
    Success ok = Success.UPDATE;
    StatusResponse latest = jpaStatusService.updateById(id, update);
    Response<Long> response =
        Response.<Long>builder()
            .code(ok.getCode())
            .message(ok.getMessage())
            .timestamp(System.currentTimeMillis())
            .payload(latest.getId())
            .build();
    return ResponseEntity.ok().body(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Response<Long>> deleteById(
      @PathVariable(name = "id", required = true) Long id) {
    Success ok = Success.DELETE;
    StatusResponse old = jpaStatusService.deleteById(id);
    Response<Long> response =
        Response.<Long>builder()
            .code(ok.getCode())
            .message(ok.getMessage())
            .timestamp(System.currentTimeMillis())
            .payload(old.getId())
            .build();
    return ResponseEntity.ok().body(response);
  }
}
