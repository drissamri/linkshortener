package be.drissamri.config.safebrowsing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class GoogleSafeBrowsingConfig {
  private static final Logger LOGGER = LoggerFactory.getLogger(GoogleSafeBrowsingConfig.class);
  private final String PARAMETER_API_KEY = "apiKey";
  private final String PARAMETER_API_VERSION = "apiVersion";
  private final String PARAMETER_APP_VERSION = "appVersion";
  private final String PARAMETER_APP_NAME = "client";
  private final Map<String, String> parameters;

  private String apiUrl;
  private boolean isConfigured;

  public GoogleSafeBrowsingConfig(String apiKey,
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

    if (!StringUtils.isEmpty(parameters.get(PARAMETER_API_KEY)) &&
      !StringUtils.isEmpty(parameters.get(PARAMETER_API_VERSION))) {

      LOGGER.info("Google Safe Browsing API v{} is configured with an API key", apiVersion);
      isConfigured = true;
    }
  }

  public String getApiUrl() {
    return apiUrl;
  }

  public Map<String, String> getParameters() {
    return new HashMap<>(parameters);
  }

  public boolean isConfigured() {
    return this.isConfigured;
  }

}
