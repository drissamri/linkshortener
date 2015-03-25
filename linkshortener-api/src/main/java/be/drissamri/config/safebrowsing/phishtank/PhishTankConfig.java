/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Driss Amri
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
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
package be.drissamri.config.safebrowsing.phishtank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

@Component
@Conditional(PhishTankCondition.class)
public class PhishTankConfig {
  private static final Logger LOGGER = LoggerFactory.getLogger(PhishTankConfig.class);
  private static final String PARAMETER_API_KEY = "app_key";
  private static final String PARAMETER_FORMAT = "format";
  private static final String RESPONSE_FORMAT = "json";
  private final String apiUrl;
  private final MultiValueMap<String, String> parameters;

  @Autowired
  public PhishTankConfig(PhishTankSettings settings) {
    this.apiUrl = settings.getEndpoint();
    this.parameters = new LinkedMultiValueMap<>();
    parameters.add(PARAMETER_API_KEY, settings.getCredential());
    parameters.add(PARAMETER_FORMAT, RESPONSE_FORMAT);

    if (!StringUtils.isEmpty(parameters.getFirst(PARAMETER_API_KEY))
      && !StringUtils.isEmpty(parameters.get(PARAMETER_FORMAT))) {
      LOGGER.info("PhishTank API is configured with an API key");
    }
  }

  public final String getApiUrl() {
    return apiUrl;
  }

  public final MultiValueMap<String, String> getParameters() {
    return new LinkedMultiValueMap<>(parameters);
  }
}
