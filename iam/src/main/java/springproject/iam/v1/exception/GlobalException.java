package springproject.iam.v1.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum GlobalException {
  UNCATEGORIZED_EXCEPTION(
      1, "uncategorized exception, service can not response.", HttpStatus.INTERNAL_SERVER_ERROR),
  METHOD_NOT_ALLOWED(1, "method not allowed on this endpoint.", HttpStatus.METHOD_NOT_ALLOWED),
  NOT_FOUND(1, "apis resource not found.", HttpStatus.NOT_FOUND),
  ILL_LEGAL_REQUEST_PAYLOAD(
      1, "missing or request payload is not readable.", HttpStatus.BAD_REQUEST),
  REQUEST_PARAMETER_NOT_READABLE(
      1, "missing or request parameter is not readable.", HttpStatus.BAD_REQUEST),
  REQUEST_BODY_NOT_READABLE(1, "missing or request body is not readable.", HttpStatus.BAD_REQUEST),
  REQUEST_HEADER_NOT_READABLE(
      1, "missing or request header is not readable.", HttpStatus.BAD_REQUEST),
  FORBIDDEN(
      1, "forbidden: do not has right authority, do not f*ck with cat.", HttpStatus.FORBIDDEN),
  IDENTITY_NOT_VERIFY(
      1, "identity not verify: missing or token is not legal.", HttpStatus.UNAUTHORIZED),
  UNAUTHORIZED(
      1,
      "ill legal token: token has been edited, expired or not publish by us.",
      HttpStatus.UNAUTHORIZED);

  int code;
  String message;
  HttpStatus httpStatus;
}
