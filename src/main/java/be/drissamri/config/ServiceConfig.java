package be.drissamri.config;

import be.drissamri.service.verifier.GoogleSafeBrowsingUrlVerifier;
import be.drissamri.service.verifier.PhishTankUrlVerifier;
import be.drissamri.service.verifier.UrlVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ServiceConfig {
  @Value("${provider.google.api}")
  private String googleApiKey;
  @Value("${provider.google.url}")
  private String googleApiUrl;
  @Value("${provider.google.version}")
  private String googleApiVersion;
  @Value("${info.build.name}")
  private String appName;
  @Value("${info.build.version}")
  private String appVersion;
  @Value("${provider.phishtank.url}")
  private String phishtankApiUrl;
  @Value("${provider.phishtank.api}")
  private String phishtankApiKey;

  @Bean
  public UrlVerifier googleSafetyProvider() {
    UrlVerifier googleUrlVerifier = null;

    if (!StringUtils.isEmpty(googleApiKey)) {
      googleUrlVerifier = new GoogleSafeBrowsingUrlVerifier(restTemplate(), googleApiKey, googleApiUrl, googleApiVersion, appName, appVersion);
    }

    return googleUrlVerifier;
  }

  @Bean
  public UrlVerifier phishTankVerifier() {
    UrlVerifier phishTankVerifier = null;

    if (!StringUtils.isEmpty(phishtankApiKey)) {
      phishTankVerifier = new PhishTankUrlVerifier(restTemplate(), phishtankApiKey, phishtankApiUrl);
    }

    return phishTankVerifier;
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

}
