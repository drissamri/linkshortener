package be.drissamri.service.verifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class GoogleSafeBrowsingUrlVerifier implements UrlVerifier {
  private static Logger LOGGER = LoggerFactory.getLogger(GoogleSafeBrowsingUrlVerifier.class);

  private static final String PARAMETER_API_KEY = "apiKey";
  private static final String PARAMETER_API_VERSION = "apiVersion";
  private static final String PARAMETER_APP_VERSION = "appVersion";
  private static final String PARAMETER_CLIENT = "client";
  private static final String PARAMETER_URL = "url";

  private String apiUrl;
  private RestTemplate restTemplate;
  private Map<String, String> formParameters;

  @Autowired
  public GoogleSafeBrowsingUrlVerifier(RestTemplate restTemplate,
                                       String apiKey,
                                       String apiUrl,
                                       String apiVersion,
                                       String appName,
                                       String appVersion) {
    this.restTemplate = restTemplate;
    this.apiUrl = apiUrl;
    this.formParameters = new HashMap<>(5);
    formParameters.put(PARAMETER_API_KEY, apiKey);
    formParameters.put(PARAMETER_API_VERSION, apiVersion);
    formParameters.put(PARAMETER_APP_VERSION, appVersion);
    formParameters.put(PARAMETER_CLIENT, appName);
  }

  public boolean isSafe(String url) {
    boolean isSafeUrl = true;

    formParameters.put(PARAMETER_URL, url);

    try {
      ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class, formParameters);
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