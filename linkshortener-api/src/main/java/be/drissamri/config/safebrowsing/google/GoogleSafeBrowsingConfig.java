/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Driss Amri
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so,| subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package be.drissamri.config.safebrowsing.google;

import be.drissamri.config.safebrowsing.ApplicationSettings;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Validate URL with the PhishTank API.
 * @author Driss Amri (drissamri@gmail.com)
 * @version $Id$
 */
@Component
@Conditional(GoogleCondition.class)
public class GoogleSafeBrowsingConfig {
  private static final Logger LOGGER = LoggerFactory.getLogger(GoogleSafeBrowsingConfig.class);
  private static final String PARAMETER_API_KEY = "apiKey";
  private static final String PARAMETER_API_VERSION = "apiVersion";
  private static final String PARAMETER_APP_VERSION = "appVersion";
  private static final String PARAMETER_APP_NAME = "client";
  private final Map<String, String> parameters;
  private final String apiUrl;

  @Autowired
  public GoogleSafeBrowsingConfig(GoogleApiSettings server, ApplicationSettings app) {
    this.parameters = populateParameterMap(server, app);
    this.apiUrl = server.getEndpoint();

    if (!StringUtils.isEmpty(parameters.get(PARAMETER_API_KEY)) &&
      !StringUtils.isEmpty(parameters.get(PARAMETER_API_VERSION))) {
      LOGGER.info("Google Safe Browsing API is configured with an API key");
    }
  }

  private HashMap<String, String> populateParameterMap(
    GoogleApiSettings server,
    ApplicationSettings app) {
    final HashMap<String, String> map = new HashMap<>();
    map.put(PARAMETER_API_KEY, server.getCredential());
    map.put(PARAMETER_API_VERSION, server.getVersion());
    map.put(PARAMETER_APP_VERSION, app.getVersion());
    map.put(PARAMETER_APP_NAME, app.getName());
    return map;
  }

  public final String getApiUrl() {
    return apiUrl;
  }

  public final Map<String, String> getParameters() {
    return new HashMap<>(parameters);
  }
}
