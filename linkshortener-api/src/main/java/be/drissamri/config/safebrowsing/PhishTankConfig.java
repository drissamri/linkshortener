package be.drissamri.config.safebrowsing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

public class PhishTankConfig {
  private static final Logger LOGGER = LoggerFactory.getLogger(PhishTankConfig.class);
  private final String PARAMETER_API_KEY = "app_key";
  private final String PARAMETER_FORMAT = "format";
  private final String RESPONSE_FORMAT = "json";
  private final String apiUrl;
  private final MultiValueMap<String, String> parameters;
  private boolean isConfigured = false;

  public PhishTankConfig(String apiKey, String apiUrl) {
    this.apiUrl = apiUrl;
    this.parameters = new LinkedMultiValueMap<>();

    parameters.add(PARAMETER_API_KEY, apiKey);
    parameters.add(PARAMETER_FORMAT, RESPONSE_FORMAT);

    if (!StringUtils.isEmpty(parameters.getFirst(PARAMETER_API_KEY)) && !StringUtils.isEmpty(parameters.get(PARAMETER_FORMAT))) {
      LOGGER.info("PhishTank API is configured with an API key");
      isConfigured = true;
    }
  }

  public String getApiUrl() {
    return apiUrl;
  }

  public MultiValueMap<String, String> getParameters() {
    return new LinkedMultiValueMap<>(parameters);
  }

  public boolean isConfigured() {
    return isConfigured;
  }

}