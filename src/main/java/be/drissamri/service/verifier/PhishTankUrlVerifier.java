package be.drissamri.service.verifier;

import be.drissamri.config.PhishTankConfig;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class PhishTankUrlVerifier implements UrlVerifier {
  private static Logger LOGGER = LoggerFactory.getLogger(GoogleSafeBrowsingUrlVerifier.class);
  private static final String PARAMETER_URL = "url";
  private static final String IN_DATABASE_PARSE_EXPRESSION = "results.in_database";
  private RestTemplate restTemplate;
  private PhishTankConfig config;

  public PhishTankUrlVerifier(RestTemplate restTemplate, PhishTankConfig config) {
    this.restTemplate = restTemplate;
    this.config = config;
  }

  @Override
  public boolean isSafe(String url) {
    boolean isSafeUrl = true;

    try {
      MultiValueMap<String, String> formParameters = config.getParameters();
      formParameters.add(PARAMETER_URL, url);
      ResponseEntity<String> response = restTemplate.postForEntity(config.getApiUrl(), formParameters, String.class);

      if (response.getStatusCode() == HttpStatus.OK) {
        boolean isPhishingUrl = JsonPath.parse(response.getBody()).read(IN_DATABASE_PARSE_EXPRESSION);
        if (isPhishingUrl) {
          isSafeUrl = false;
        }
      } else {
        LOGGER.warn("Request for PhishTank API failed with status: {} - : {}", response.getStatusCode(), response);
      }
    } catch (RestClientException | PathNotFoundException exception) {
      LOGGER.warn("Something went wrong while processing PhishTank API: {}", exception);
    }

    return isSafeUrl;
  }
}
