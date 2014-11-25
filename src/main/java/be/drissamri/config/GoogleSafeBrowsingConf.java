package be.drissamri.config;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class GoogleSafeBrowsingConf {
  private final String PARAMETER_API_KEY = "apiKey";
  private final String PARAMETER_API_VERSION = "apiVersion";
  private final String PARAMETER_APP_VERSION = "appVersion";
  private final String PARAMETER_APP_NAME = "client";
  private final Map<String, String> parameters;

  private String apiUrl;

  public GoogleSafeBrowsingConf(String apiKey,
                                String apiUrl,
                                String apiVersion,
                                String appName,
                                String appVersion) {

    this.parameters = new HashMap<>();
    parameters.put(PARAMETER_API_KEY, apiKey);
    parameters.put(PARAMETER_API_VERSION, apiVersion);
    parameters.put(PARAMETER_APP_VERSION, appVersion);
    parameters.put(PARAMETER_APP_NAME, appName);

    this.apiUrl = apiUrl;
  }

  public String getApiUrl() {
    return apiUrl;
  }

  public Map<String, String> getParameters() {
    return new HashMap<>(parameters);
  }

  public boolean isConfigured() {
    boolean isConfigured = false;
    if (!StringUtils.isEmpty(parameters.get(PARAMETER_API_KEY)) &&
      !StringUtils.isEmpty(parameters.get(PARAMETER_API_VERSION))) {

      isConfigured = true;
    }

    return isConfigured;
  }
}
