package springproject.gateway.v1.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException.FeignClientException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import springproject.gateway.v1.constant.Failed;
import springproject.gateway.v1.exception.GlobalException;
import springproject.gateway.v1.exception.ServiceException;
import springproject.gateway.v1.response.Response;
import springproject.gateway.v1.service.iam.IamServiceClient;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationFilter extends OncePerRequestFilter {
  @NonFinal List<String> publicEndpoints = List.of("/api/v1/auth/sign-in");

  IamServiceClient iamServiceClient;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    try {
      String url = request.getRequestURI();
      boolean isPublicEndpoint = this.isPublicEndpoint(url, this.publicEndpoints);
      if (isPublicEndpoint) {
        filterChain.doFilter(request, response);
        return;
      }
      String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
      String refreshTokenHeader = request.getHeader("X-REFRESH-TOKEN");
      boolean isMissingTokenHeader =
          StringUtils.isEmpty(authorizationHeader) || StringUtils.isEmpty(refreshTokenHeader);
      if (isMissingTokenHeader) {
        Failed unauthorized = Failed.TOKEN_HEADER_MISSING;
        immediatelyTerminate(
            response, HttpStatus.UNAUTHORIZED, unauthorized.getCode(), unauthorized.getMessage());
        return;
      }
      String accessToken = authorizationHeader.replace("Bearer ", "");
      String refreshToken = refreshTokenHeader.replace("Bearer ", "");
      ResponseEntity<Response<Boolean>> identityResponse =
          iamServiceClient.identity(accessToken, refreshToken);
      boolean isAuthenticated = identityResponse.getBody().getPayload();
      if (!isAuthenticated) {
        GlobalException unauthorized = GlobalException.IDENTITY_NOT_VERIFY;
        immediatelyTerminate(
            response, HttpStatus.UNAUTHORIZED, unauthorized.getCode(), unauthorized.getMessage());
        return;
      }
      filterChain.doFilter(request, response);
    } catch (ServiceException e) {
      e.printStackTrace();
      if (e.getFailed().getHttpStatus().isError()) {
        Failed failed = e.getFailed();
        immediatelyTerminate(
            response, failed.getHttpStatus(), failed.getCode(), failed.getMessage());
      }
      filterChain.doFilter(request, response);
    } catch (FeignClientException e) {
      Failed communication = Failed.SERVICE_COMMUNICATION;
      immediatelyTerminate(
          response,
          communication.getHttpStatus(),
          communication.getCode(),
          communication.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
      GlobalException communication = GlobalException.COMMUNICATION_ESTABLISHED;
      immediatelyTerminate(
          response,
          communication.getHttpStatus(),
          communication.getCode(),
          communication.getMessage());
    }
  }

  public boolean isPublicEndpoint(String url, List<String> publicEndpoints) {
    Pattern.compile(url + "/*");
    return publicEndpoints.stream().anyMatch(v -> v.contains(url));
  }

  private void immediatelyTerminate(
      HttpServletResponse response, HttpStatus httpStatus, int code, String message)
      throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(httpStatus.value());
    Response<Object> resp =
        Response.builder()
            .code(code)
            .message(message)
            .timestamp(System.currentTimeMillis())
            .build();
    response.getWriter().write(objectMapper.writeValueAsString(resp));
    response.flushBuffer();
  }
}
