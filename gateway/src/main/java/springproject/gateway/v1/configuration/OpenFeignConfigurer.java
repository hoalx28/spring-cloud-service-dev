package springproject.gateway.v1.configuration;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.codec.ErrorDecoder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import springproject.gateway.v1.exception.ServiceException;
import springproject.shared.v1.constant.Failed;
import springproject.shared.v1.response.PagingResponse;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OpenFeignConfigurer implements ErrorDecoder, RequestInterceptor {
  ObjectMapper objectMapper;

  @Override
  public Exception decode(String methodKey, feign.Response response) {
    PagingResponse<Object> resp = new PagingResponse<>();
    try (InputStream bodyIs = response.body().asInputStream()) {
      HttpStatus statusCode = HttpStatus.valueOf(response.status());
      Set<Entry<HttpStatus, Failed>> useCases = new HashSet<>();
      useCases.add(Map.entry(HttpStatus.FORBIDDEN, Failed.PART_FORBIDDEN));
      useCases.add(Map.entry(HttpStatus.NOT_FOUND, Failed.SERVICE_COMMUNICATION_NOT_FOUND));
      for (Entry<HttpStatus, Failed> useCase : useCases) {
        if (statusCode.isSameCodeAs(useCase.getKey())) {
          return new ServiceException(useCase.getValue());
        }
      }
      resp = objectMapper.readValue(bodyIs, PagingResponse.class);
      for (Failed failed : Failed.values()) {
        if (failed.getCode() == resp.getCode()) {
          return new ServiceException(failed);
        }
      }
      return new Default().decode(methodKey, response);
    } catch (Exception e) {
      e.printStackTrace();
      return new ServiceException(Failed.SERVICE_COMMUNICATION);
    }
  }

  @Override
  public void apply(RequestTemplate template) {
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    String refreshTokenHeader = request.getHeader("X-REFRESH-TOKEN");
    if (StringUtils.isNotBlank(authorizationHeader)) {
      template.header(HttpHeaders.AUTHORIZATION, authorizationHeader);
    }
    if (StringUtils.isNotBlank(refreshTokenHeader)) {
      template.header("X-REFRESH-TOKEN", refreshTokenHeader);
    }
  }
}
