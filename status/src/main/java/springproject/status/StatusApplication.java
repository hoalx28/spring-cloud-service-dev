package springproject.status;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class StatusApplication {

  public static void main(String[] args) {
    SpringApplication.run(StatusApplication.class, args);
  }
}
