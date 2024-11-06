package springproject.iam.v1.constant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum Success {
  SAVE(1, "{resource} has been saved.", HttpStatus.CREATED),
  FIND_BY_ID(1, "query {resource} by id success.", HttpStatus.OK),
  FIND_ALL(1, "query {resource}s success.", HttpStatus.OK),
  FIND_ALL_BY(1, "query {resource} by {criteria} success.", HttpStatus.OK),
  UPDATE(1, "{resource} has been updated.", HttpStatus.OK),
  DELETE(1, "{resource} has been deleted.", HttpStatus.OK),

  SIGN_UP(1, "sign up success, enjoy.", HttpStatus.CREATED),
  SIGN_IN(1, "sign in success, enjoy.", HttpStatus.OK),
  VERIFY_IDENTITY(1, "identity has been verified, enjoy.", HttpStatus.OK),
  SIGN_OUT(1, "sign out success, enjoy.", HttpStatus.OK),
  REFRESH_JWT_TOKEN(1, "refresh token success, enjoy.", HttpStatus.OK),
  RETRIEVE_PROFILE(1, "retrieve profile success, enjoy.", HttpStatus.OK);

  int code;
  String message;
  HttpStatus httpStatus;
}
