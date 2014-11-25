package be.drissamri.config;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

public class PhishTankConfig {
  private final String PARAMETER_API_KEY = "app_key";
  private final String PARAMETER_FORMAT = "format";
  private final String RESPONSE_FORMAT = "json";
  private final String apiUrl;
  private final MultiValueMap<String, String> parameters;

  public PhishTankConfig(String apiKey, String apiUrl) {
    this.apiUrl = apiUrl;
    this.parameters = new LinkedMultiValueMap<>();

    parameters.add(PARAMETER_API_KEY, apiKey);
    parameters.add(PARAMETER_FORMAT, RESPONSE_FORMAT);
  }

  public String getApiUrl() {
    return apiUrl;
  }

  public MultiValueMap<String, String> getParameters() {
    return new LinkedMultiValueMap<>(parameters);
  }

  public boolean isConfigured() {
    boolean isConfigured = false;

    if (!StringUtils.isEmpty(parameters.get(PARAMETER_API_KEY)) &&
      !StringUtils.isEmpty(parameters.get(PARAMETER_FORMAT))) {

      isConfigured = true;
    }

    return isConfigured;
  }

}
