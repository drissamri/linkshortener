package be.drissamri.service.verifier;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class PhishTankUrlVerifier implements UrlVerifier {
  private static Logger LOGGER = LoggerFactory.getLogger(GoogleSafeBrowsingUrlVerifier.class);
  private static final String PARAMETER_API_KEY = "app_key";
  private static final String PARAMETER_FORMAT = "format";
  private static final String PARAMETER_URL = "url";
  private static final String JSON_FORMAT = "json";
  private static final String IN_DATABASE_PARSE_EXPRESSION = "results.in_database";

  private String apiUrl;
  private RestTemplate restTemplate;
  private MultiValueMap<String, String> formParameters;


  @Autowired
  public PhishTankUrlVerifier(RestTemplate restTemplate, String apiKey, String apiUrl) {
    this.restTemplate = restTemplate;
    this.apiUrl = apiUrl;
    this.formParameters = new LinkedMultiValueMap<>();
    formParameters.add(PARAMETER_API_KEY, apiKey);
    formParameters.add(PARAMETER_FORMAT, JSON_FORMAT);
  }

  @Override
  public boolean isSafe(String url) {
    boolean isSafeUrl = true;

    try {
      formParameters.add(PARAMETER_URL, url);
      ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, formParameters, String.class);

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
