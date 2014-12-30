package be.drissamri.config;

import be.drissamri.rest.LinkController;
import be.drissamri.rest.RedirectController;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {

  public JerseyConfig() {
    register(LinkController.class);
    register(RedirectController.class);
  }
}
