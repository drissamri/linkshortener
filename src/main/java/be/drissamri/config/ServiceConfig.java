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
  @Value("${provider.google.api}")      private String googleApiKey;
  @Value("${provider.google.url}")      private String googleApiUrl;
  @Value("${provider.google.version}")  private String googleApiVersion;
  @Value("${provider.phishtank.url}")   private String phishtankApiUrl;
  @Value("${provider.phishtank.api}")   private String phishtankApiKey;
  @Value("${info.build.name}")          private String appName;
  @Value("${info.build.version}")       private String appVersion;

  @Bean
  public UrlVerifier googleSafetyProvider() {
    UrlVerifier googleUrlVerifier = null;

    if (!StringUtils.isEmpty(googleApiKey)) {
      GoogleSafeBrowsingConfig config = new GoogleSafeBrowsingConfig(googleApiKey, googleApiUrl, googleApiVersion, appName, appVersion);
      googleUrlVerifier = new GoogleSafeBrowsingUrlVerifier(restTemplate(), config);
    }

    return googleUrlVerifier;
  }

  @Bean
  public UrlVerifier phishTankVerifier() {
    UrlVerifier phishTankVerifier = null;

    if (!StringUtils.isEmpty(phishtankApiKey)) {
      PhishTankConfig config = new PhishTankConfig(phishtankApiKey, phishtankApiKey);
      phishTankVerifier = new PhishTankUrlVerifier(restTemplate(), config);
    }

    return phishTankVerifier;
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

}
