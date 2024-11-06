package springproject.gateway.v1.exception;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import springproject.gateway.v1.constant.Failed;

@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ServiceException extends RuntimeException {
  Failed failed;
}