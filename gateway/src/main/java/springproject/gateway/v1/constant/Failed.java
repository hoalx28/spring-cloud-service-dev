package springproject.gateway.v1.constant;

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
      1, "{resource} already existed, {identity} must be unique.", HttpStatus.BAD_REQUEST),
  SAVE(1, "can not save {resource}: try again later.", HttpStatus.INTERNAL_SERVER_ERROR),
  FIND_BY_ID(
      1, "can not retrieve {resource} by id: try again later.", HttpStatus.INTERNAL_SERVER_ERROR),
  FIND_BY_ID_NO_CONTENT(1, "retrieve {resource} by id return no content.", HttpStatus.NO_CONTENT),
  FIND_BY_USERNAME(
      1,
      "can not retrieve {resource} by username: try again later.",
      HttpStatus.INTERNAL_SERVER_ERROR),
  FIND_ALL(1, "can not retrieve {resource}: try again later.", HttpStatus.INTERNAL_SERVER_ERROR),
  FIND_ALL_NO_CONTENT(1, "retrieve {resource} return no content.", HttpStatus.NO_CONTENT),
  FIND_ALL_BY(
      1,
      "can not retrieve {resource} by {criteria}: try again later.",
      HttpStatus.INTERNAL_SERVER_ERROR),
  FIND_ALL_BY_NO_CONTENT(
      1, "retrieve {resource}s by {criteria} return no content.", HttpStatus.NO_CONTENT),
  NOT_EXISTS(1, "{resource} does not existed.", HttpStatus.NOT_MODIFIED),
  UPDATE(1, "can not update {resource}: try again later.", HttpStatus.INTERNAL_SERVER_ERROR),
  DELETE(1, "can not delete {resource}: try again later.", HttpStatus.INTERNAL_SERVER_ERROR),
  OWNING_SIDE_NOT_EXISTS(
      1, "can not save {resource}: {owning side} not exists.", HttpStatus.BAD_REQUEST),
  OWNING_SIDE_NOT_AVAILABLE(
      1, "can not save {resource}: {owning side} not available.", HttpStatus.BAD_REQUEST),

  SIGN_JWT_TOKEN(
      1,
      "can not sign token: ill legal claims or encrypt algorithm.",
      HttpStatus.INTERNAL_SERVER_ERROR),
  PARSE_JWT_TOKEN(
      1,
      "can not parse token: ill legal token or encrypt algorithm.",
      HttpStatus.INTERNAL_SERVER_ERROR),
  JWT_TOKEN_EXPIRED(
      1, "token has been expired: sign in again to get new one.", HttpStatus.UNAUTHORIZED),
  ILL_LEGAL_JWT_TOKEN(
      1, "ill legal token: token has been edited or not publish by us.", HttpStatus.UNAUTHORIZED),

  SIGN_UP(1, "can not sign up: try again later.", HttpStatus.INTERNAL_SERVER_ERROR),
  SIGN_IN(1, "can not sign in: try again later.", HttpStatus.INTERNAL_SERVER_ERROR),
  BAD_CREDENTIALS(1, "bad credentials: username or password not match.", HttpStatus.UNAUTHORIZED),
  JWT_TOKEN_NOT_SUITABLE(
      1, "access token and refresh token are not suitable.", HttpStatus.CONFLICT),
  VERIFY_IDENTITY(1, "can not verify identity: try again later.", HttpStatus.INTERNAL_SERVER_ERROR),
  JWT_TOKEN_BLOCKED(1, "token has been recall: can not use this any more", HttpStatus.UNAUTHORIZED),
  ENSURE_JWT_TOKEN_NOT_BAD_CREDENTIALS(
      1, "can not ensure token is not recall.", HttpStatus.INTERNAL_SERVER_ERROR),
  SIGN_OUT(1, "can not sign out: token not be recalled.", HttpStatus.INTERNAL_SERVER_ERROR),
  REFRESH_JWT_TOKEN(1, "can not refresh token: try again later.", HttpStatus.INTERNAL_SERVER_ERROR),
  RETRIEVE_PROFILE(
      1, "can not retrieve profile: try again later.", HttpStatus.INTERNAL_SERVER_ERROR),
  MAIL_DELIVERY(
      1,
      "mail delivery failed: make sure you type correct address and content encoding was supported.",
      HttpStatus.INTERNAL_SERVER_ERROR);

  int code;
  String message;
  HttpStatus httpStatus;
}
