package springproject.status.v1.exception;

import feign.RetryableException;
import java.text.MessageFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import springproject.status.v1.constant.Failed;
import springproject.status.v1.response.Response;

@ControllerAdvice
public class ServiceExceptionResolver {

  @ExceptionHandler(value = Exception.class)
  public ResponseEntity<Response<Object>> uncategorized(Exception exception) {
    GlobalException uncategorized = GlobalException.UNCATEGORIZED_EXCEPTION;
    Response<Object> response =
        Response.builder()
            .code(uncategorized.getCode())
            .message(uncategorized.getMessage())
            .timestamp(System.currentTimeMillis())
            .build();
    return ResponseEntity.status(uncategorized.getHttpStatus()).body(response);
  }

  @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<Response<Object>> methodNotAllowed(
      HttpRequestMethodNotSupportedException exception) {
    GlobalException methodNotAllowed = GlobalException.METHOD_NOT_ALLOWED;
    Response<Object> response =
        Response.builder()
            .code(methodNotAllowed.getCode())
            .message(methodNotAllowed.getMessage())
            .timestamp(System.currentTimeMillis())
            .build();
    return ResponseEntity.status(methodNotAllowed.getHttpStatus()).body(response);
  }

  @ExceptionHandler(value = NoHandlerFoundException.class)
  public ResponseEntity<Response<Object>> notFound(NoHandlerFoundException exception) {
    GlobalException notFound = GlobalException.NOT_FOUND;
    Response<Object> response =
        Response.builder()
            .code(notFound.getCode())
            .message(notFound.getMessage())
            .timestamp(System.currentTimeMillis())
            .build();
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  public ResponseEntity<Response<Object>> invalidRequest(
      MethodArgumentNotValidException exception) {
    GlobalException badRequest = GlobalException.ILL_LEGAL_REQUEST_PAYLOAD;
    String message = exception.getAllErrors().get(0).getDefaultMessage();
    Response<Object> response =
        Response.builder()
            .code(badRequest.getCode())
            .message(message)
            .timestamp(System.currentTimeMillis())
            .build();
    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(value = HandlerMethodValidationException.class)
  public ResponseEntity<Response<Object>> invalidRequest(
      HandlerMethodValidationException exception) {
    GlobalException badRequest = GlobalException.ILL_LEGAL_REQUEST_PAYLOAD;
    String message = exception.getAllErrors().get(0).getDefaultMessage();
    Response<Object> response =
        Response.builder()
            .code(badRequest.getCode())
            .message(message)
            .timestamp(System.currentTimeMillis())
            .build();
    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(value = MissingServletRequestParameterException.class)
  public ResponseEntity<Response<Object>> missingRequestParameter(
      MissingServletRequestParameterException exception) {
    GlobalException badRequest = GlobalException.REQUEST_PARAMETER_NOT_READABLE;
    String message =
        MessageFormat.format("{0} is missing in query string.", exception.getParameterName());
    Response<Object> response =
        Response.builder()
            .code(badRequest.getCode())
            .message(message)
            .timestamp(System.currentTimeMillis())
            .build();
    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
  public ResponseEntity<Response<Object>> missingRequestParameter(
      MethodArgumentTypeMismatchException exception) {
    GlobalException badRequest = GlobalException.REQUEST_PARAMETER_NOT_READABLE;
    String message = MessageFormat.format("{0} is not readable.", exception.getPropertyName());
    Response<Object> response =
        Response.builder()
            .code(badRequest.getCode())
            .message(message)
            .timestamp(System.currentTimeMillis())
            .build();
    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(value = HttpMessageNotReadableException.class)
  public ResponseEntity<Response<Object>> missingRequestBody(
      HttpMessageNotReadableException exception) {
    GlobalException badRequest = GlobalException.REQUEST_BODY_NOT_READABLE;
    Response<Object> response =
        Response.builder()
            .code(badRequest.getCode())
            .message(badRequest.getMessage())
            .timestamp(System.currentTimeMillis())
            .build();
    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(value = MissingRequestHeaderException.class)
  public ResponseEntity<Response<Object>> missingRequestBody(
      MissingRequestHeaderException exception) {
    GlobalException badRequest = GlobalException.REQUEST_HEADER_NOT_READABLE;
    String message =
        MessageFormat.format("{0} is missing in request header.", exception.getHeaderName());
    Response<Object> response =
        Response.builder()
            .code(badRequest.getCode())
            .message(message)
            .timestamp(System.currentTimeMillis())
            .build();
    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(value = AuthorizationDeniedException.class)
  public ResponseEntity<Response<Object>> forbidden(AuthorizationDeniedException exception) {
    GlobalException forbidden = GlobalException.FORBIDDEN;
    Response<Object> response =
        Response.builder()
            .code(forbidden.getCode())
            .message(forbidden.getMessage())
            .timestamp(System.currentTimeMillis())
            .build();
    return ResponseEntity.status(forbidden.getHttpStatus()).body(response);
  }

  @ExceptionHandler(value = RetryableException.class)
  public ResponseEntity<Response<Object>> forbidden(RetryableException exception) {
    GlobalException communication = GlobalException.COMMUNICATION_ESTABLISHED;
    Response<Object> response =
        Response.builder()
            .code(communication.getCode())
            .message(communication.getMessage())
            .timestamp(System.currentTimeMillis())
            .build();
    return ResponseEntity.status(communication.getHttpStatus()).body(response);
  }

  @ExceptionHandler(value = ServiceException.class)
  public ResponseEntity<Response<Object>> service(ServiceException exception) {
    Failed failed = exception.getFailed();
    Response<Object> response =
        Response.builder()
            .code(failed.getCode())
            .message(failed.getMessage())
            .timestamp(System.currentTimeMillis())
            .build();
    return ResponseEntity.status(failed.getHttpStatus()).body(response);
  }
}
