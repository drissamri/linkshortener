package be.drissamri;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableJpaAuditing
public class LinkshortenerApp {

  public static void main(String[] args) {
    SpringApplication.run(LinkshortenerApp.class, args);
  }

  @Order(Ordered.HIGHEST_PRECEDENCE)
  @Configuration
  protected static class AuthenticationSecurity extends GlobalAuthenticationConfigurerAdapter {

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
      // @formatter:off
		auth.inMemoryAuthentication()
            .withUser("admin").password("pass")
              .roles("USER");
	  // @formatter:on
    }
  }
}