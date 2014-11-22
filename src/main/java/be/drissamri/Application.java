package be.drissamri;

import be.drissamri.service.verifier.GoogleSafeBrowsingUrlVerifier;
import be.drissamri.service.verifier.PhishTankUrlVerifier;
import be.drissamri.service.verifier.UrlVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableJpaAuditing
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}