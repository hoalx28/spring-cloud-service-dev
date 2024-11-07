package springproject.status.v1.constant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum Failed {
  ALREADY_EXISTED(
      1001, "{resource} already existed, {identity} must be unique.", HttpStatus.BAD_REQUEST),
  SAVE(1002, "can not save {resource}: try again later.", HttpStatus.INTERNAL_SERVER_ERROR),
  FIND_BY_ID(
      1003,
      "can not retrieve {resource} by id: try again later.",
      HttpStatus.INTERNAL_SERVER_ERROR),
  FIND_BY_ID_NO_CONTENT(1, "retrieve {resource} by id return no content.", HttpStatus.NO_CONTENT),
  FIND_BY_USERNAME(
      1004,
      "can not retrieve {resource} by username: try again later.",
      HttpStatus.INTERNAL_SERVER_ERROR),
  FIND_ALL(1005, "can not retrieve {resource}: try again later.", HttpStatus.INTERNAL_SERVER_ERROR),
  FIND_ALL_NO_CONTENT(1006, "retrieve {resource} return no content.", HttpStatus.NO_CONTENT),
  FIND_ALL_BY(
      1007,
      "can not retrieve {resource} by {criteria}: try again later.",
      HttpStatus.INTERNAL_SERVER_ERROR),
  FIND_ALL_BY_NO_CONTENT(
      1008, "retrieve {resource}s by {criteria} return no content.", HttpStatus.NO_CONTENT),
  NOT_EXISTS(1009, "{resource} does not existed.", HttpStatus.NOT_MODIFIED),
  UPDATE(1010, "can not update {resource}: try again later.", HttpStatus.INTERNAL_SERVER_ERROR),
  DELETE(1011, "can not delete {resource}: try again later.", HttpStatus.INTERNAL_SERVER_ERROR),
  OWNING_SIDE_NOT_EXISTS(
      1012, "can not save {resource}: {owning side} not exists.", HttpStatus.BAD_REQUEST),
  OWNING_SIDE_NOT_AVAILABLE(
      1013, "can not save {resource}: {owning side} not available.", HttpStatus.BAD_REQUEST),

  SIGN_JWT_TOKEN(
      1014,
      "can not sign token: ill legal claims or encrypt algorithm.",
      HttpStatus.INTERNAL_SERVER_ERROR),
  PARSE_JWT_TOKEN(
      1015,
      "can not parse token: ill legal token or encrypt algorithm.",
      HttpStatus.INTERNAL_SERVER_ERROR),
  JWT_TOKEN_EXPIRED(
      1016, "token has been expired: sign in again to get new one.", HttpStatus.UNAUTHORIZED),
  ILL_LEGAL_JWT_TOKEN(
      1017,
      "ill legal token: token has been edited or not publish by us.",
      HttpStatus.UNAUTHORIZED),

  SIGN_UP(1018, "can not sign up: try again later.", HttpStatus.INTERNAL_SERVER_ERROR),
  SIGN_IN(1019, "can not sign in: try again later.", HttpStatus.INTERNAL_SERVER_ERROR),
  BAD_CREDENTIALS(
      1020, "bad credentials: username or password not match.", HttpStatus.UNAUTHORIZED),
  JWT_TOKEN_NOT_SUITABLE(
      1021, "access token and refresh token are not suitable.", HttpStatus.CONFLICT),
  VERIFY_IDENTITY(
      1022, "can not verify identity: try again later.", HttpStatus.INTERNAL_SERVER_ERROR),
  JWT_TOKEN_BLOCKED(
      1023, "token has been recall: can not use this any more", HttpStatus.UNAUTHORIZED),
  ENSURE_JWT_TOKEN_NOT_BAD_CREDENTIALS(
      1024, "can not ensure token is not recall.", HttpStatus.INTERNAL_SERVER_ERROR),
  SIGN_OUT(1025, "can not sign out: token not be recalled.", HttpStatus.INTERNAL_SERVER_ERROR),
  REFRESH_JWT_TOKEN(
      1026, "can not refresh token: try again later.", HttpStatus.INTERNAL_SERVER_ERROR),
  RETRIEVE_PROFILE(
      1027, "can not retrieve profile: try again later.", HttpStatus.INTERNAL_SERVER_ERROR),

  TOKEN_HEADER_MISSING(
      1028,
      "missing token header: authorization and x-refresh-token header are required.",
      HttpStatus.SERVICE_UNAVAILABLE),

  PART_FORBIDDEN(
      1029,
      "forbidden: do not has right authority to access part of this resource.",
      HttpStatus.FORBIDDEN),
  SERVICE_COMMUNICATION(
      1030, "error occur when communicate between services.", HttpStatus.INTERNAL_SERVER_ERROR),
  SERVICE_COMMUNICATION_NOT_FOUND(
      1031, "can not communicate: target endpoint not found.", HttpStatus.INTERNAL_SERVER_ERROR);

  int code;
  String message;
  HttpStatus httpStatus;
}
