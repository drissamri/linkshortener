package be.drissamri.service.verifier;

import be.drissamri.config.GoogleSafeBrowsingConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class GoogleSafeBrowsingUrlVerifier implements UrlVerifier {
  private static Logger LOGGER = LoggerFactory.getLogger(GoogleSafeBrowsingUrlVerifier.class);
  private static final String PARAMETER_URL = "url";
  private GoogleSafeBrowsingConfig config;
  private RestTemplate restTemplate;

  public GoogleSafeBrowsingUrlVerifier(RestTemplate restTemplate, GoogleSafeBrowsingConfig config) {
    this.restTemplate = restTemplate;
    this.config = config;
  }

  public boolean isSafe(String url) {
    boolean isSafeUrl = true;
    Map<String, String> formParameters = config.getParameters();
    formParameters.put(PARAMETER_URL, url);

    try {
      ResponseEntity<String> response = restTemplate.getForEntity(config.getApiUrl(), String.class, formParameters);
      LOGGER.debug("Google Safe Browsing API returned HTTP Status: {} and message: {}", response.getStatusCode(), response.getBody());

      if (response.getStatusCode() == HttpStatus.OK) {
        LOGGER.debug("Possible unsafe link: {} - {}", url, response.getBody());
        isSafeUrl = false;
      }

    } catch (RestClientException restException) {
      LOGGER.warn("Exception occurred during HTTP call to Google Safe Browsing API: {}", restException);
    }
    return isSafeUrl;
  }
}