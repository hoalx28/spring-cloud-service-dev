package springproject.iam.v1.controller.shared;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springproject.iam.v1.constant.Success;
import springproject.iam.v1.model.dto.auth.CredentialRequest;
import springproject.iam.v1.model.dto.auth.CredentialResponse;
import springproject.iam.v1.model.dto.auth.RegisterRequest;
import springproject.iam.v1.model.dto.auth.RegisterResponse;
import springproject.iam.v1.response.Response;
import springproject.iam.v1.service.auth.AbstractJwtAuthService;

@RestController
@RequestMapping(path = "/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
  AbstractJwtAuthService jwtAuthService;

  @PostMapping("/sign-up")
  public ResponseEntity<Response<CredentialResponse>> signUp(
      @RequestBody @Valid RegisterRequest request) {
    Success created = Success.SIGN_UP;
    CredentialResponse credential = jwtAuthService.signUp(request);

    Response<CredentialResponse> response =
        Response.<CredentialResponse>builder()
            .code(created.getCode())
            .message(created.getMessage())
            .timestamp(System.currentTimeMillis())
            .payload(credential)
            .build();
    return ResponseEntity.status(created.getHttpStatus()).body(response);
  }

  @PostMapping("/sign-in")
  public ResponseEntity<Response<CredentialResponse>> signIn(
      @RequestBody @Valid CredentialRequest request) {
    Success ok = Success.SIGN_IN;
    CredentialResponse credential = jwtAuthService.signIn(request);

    Response<CredentialResponse> response =
        Response.<CredentialResponse>builder()
            .code(ok.getCode())
            .message(ok.getMessage())
            .timestamp(System.currentTimeMillis())
            .payload(credential)
            .build();
    return ResponseEntity.ok().body(response);
  }

  @PostMapping("/identity")
  public ResponseEntity<Response<Boolean>> identity(
      @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = true) String accessToken,
      @RequestHeader(value = "X-REFRESH-TOKEN", required = true) String refreshToken) {
    Success ok = Success.VERIFY_IDENTITY;
    accessToken = accessToken.replace("Bearer ", "");
    refreshToken = refreshToken.replace("Bearer ", "");
    boolean isVerified = jwtAuthService.identity(accessToken, refreshToken);

    Response<Boolean> response =
        Response.<Boolean>builder()
            .code(ok.getCode())
            .message(ok.getMessage())
            .timestamp(System.currentTimeMillis())
            .payload(isVerified)
            .build();
    return ResponseEntity.ok().body(response);
  }

  @PostMapping("/sign-out")
  public ResponseEntity<Response<Long>> signOut() {
    Success created = Success.SIGN_OUT;
    long id = jwtAuthService.signOut();

    Response<Long> response =
        Response.<Long>builder()
            .code(created.getCode())
            .message(created.getMessage())
            .timestamp(System.currentTimeMillis())
            .payload(id)
            .build();
    return ResponseEntity.status(created.getHttpStatus()).body(response);
  }

  @PostMapping("/refresh")
  public ResponseEntity<Response<CredentialResponse>> refresh(
      @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = true) String accessToken,
      @RequestHeader(value = "X-REFRESH-TOKEN", required = true) String refreshToken) {
    Success ok = Success.REFRESH_JWT_TOKEN;
    accessToken = accessToken.replace("Bearer ", "");
    refreshToken = refreshToken.replace("Bearer ", "");
    CredentialResponse credential = jwtAuthService.refresh(accessToken, refreshToken);

    Response<CredentialResponse> response =
        Response.<CredentialResponse>builder()
            .code(ok.getCode())
            .message(ok.getMessage())
            .timestamp(System.currentTimeMillis())
            .payload(credential)
            .build();
    return ResponseEntity.ok().body(response);
  }

  @GetMapping("/me")
  public ResponseEntity<Response<RegisterResponse>> me(
      @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = true) String accessToken,
      @AuthenticationPrincipal UserDetails userDetails) {
    Success ok = Success.RETRIEVE_PROFILE;
    RegisterResponse user = jwtAuthService.me();

    Response<RegisterResponse> response =
        Response.<RegisterResponse>builder()
            .code(ok.getCode())
            .message(ok.getMessage())
            .timestamp(System.currentTimeMillis())
            .payload(user)
            .build();
    return ResponseEntity.ok().body(response);
  }
}
