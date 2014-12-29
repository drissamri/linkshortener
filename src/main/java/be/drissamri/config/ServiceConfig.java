package be.drissamri.config;

import be.drissamri.config.safebrowsing.GoogleSafeBrowsingConfig;
import be.drissamri.config.safebrowsing.PhishTankConfig;
import be.drissamri.service.verifier.GoogleSafeBrowsingUrlVerifier;
import be.drissamri.service.verifier.PhishTankUrlVerifier;
import be.drissamri.service.verifier.UrlVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ServiceConfig {
  @Value("${provider.google.api}")
  private String googleApiKey;
  @Value("${provider.google.url}")
  private String googleApiUrl;
  @Value("${provider.google.version}")
  private String googleApiVersion;
  @Value("${provider.phishtank.url}")
  private String phishtankApiUrl;
  @Value("${provider.phishtank.api}")
  private String phishtankApiKey;
  @Value("${info.build.name}")
  private String appName;
  @Value("${info.build.version}")
  private String appVersion;

  @Bean
  public UrlVerifier googleSafetyProvider() {
    GoogleSafeBrowsingConfig config = new GoogleSafeBrowsingConfig(googleApiKey, googleApiUrl, googleApiVersion, appName, appVersion);
    return new GoogleSafeBrowsingUrlVerifier(restTemplate(), config);
  }

  @Bean
  public UrlVerifier phishTankVerifier() {
    PhishTankConfig config = new PhishTankConfig(phishtankApiKey, phishtankApiUrl);
    return new PhishTankUrlVerifier(restTemplate(), config);
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

}
