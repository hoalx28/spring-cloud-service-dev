package springproject.gateway.v1.service.iam;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import springproject.gateway.v1.configuration.OpenFeignConfigurer;
import springproject.shared.v1.response.Response;

@FeignClient(name = "iam", configuration = { OpenFeignConfigurer.class })
public interface IamServiceClient {

    @PostMapping("/auth/identity")
    ResponseEntity<Response<Boolean>> identity(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = true) String accessToken,
            @RequestHeader(value = "X-REFRESH-TOKEN", required = true) String refreshToken);
}
