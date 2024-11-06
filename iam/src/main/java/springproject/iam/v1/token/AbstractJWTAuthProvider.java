package springproject.iam.v1.token;

public interface AbstractJWTAuthProvider<T, U> {
  String buildScope(U user);

  String sign(U user, long expiredTime, String secretKey, String id, String referId);

  T verify(String token, String secretKey);
}
