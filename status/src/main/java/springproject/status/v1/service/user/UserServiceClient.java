package springproject.status.v1.service.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import springproject.status.v1.configuration.OpenFeignConfigurer;
import springproject.status.v1.model.dto.user.UserResponse;
import springproject.status.v1.response.Response;

@FeignClient(name = "iam", configuration = { OpenFeignConfigurer.class })
@Service
public interface UserServiceClient {
    @GetMapping(value = "/users/{id}")
    ResponseEntity<Response<UserResponse>> findById(
            @PathVariable(name = "id", required = true) Long id);
}
