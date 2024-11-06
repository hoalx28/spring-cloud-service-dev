package springproject.status.v1.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import springproject.status.v1.response.Response;

public class OauthResourceJwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException, ServletException {
    GlobalException unauthorized = GlobalException.UNAUTHORIZED;
    if (authException instanceof InsufficientAuthenticationException) {
      unauthorized = GlobalException.IDENTITY_NOT_VERIFY;
    }
    ObjectMapper objectMapper = new ObjectMapper();
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    Response<Object> resp =
        Response.builder()
            .code(unauthorized.getCode())
            .message(unauthorized.getMessage())
            .timestamp(System.currentTimeMillis())
            .build();
    response.getWriter().write(objectMapper.writeValueAsString(resp));
    response.flushBuffer();
  }
}
