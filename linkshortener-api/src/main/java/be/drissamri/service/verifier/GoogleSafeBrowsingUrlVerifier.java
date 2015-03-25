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
package be.drissamri.service.verifier;

import be.drissamri.config.safebrowsing.google.GoogleCondition;
import be.drissamri.config.safebrowsing.google.GoogleSafeBrowsingConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Validate URL with the Google Safe Browsing API.
 * @author Driss Amri (drissamri@gmail.com)
 * @version $Id$
 */
@Component
@Conditional(GoogleCondition.class)
public class GoogleSafeBrowsingUrlVerifier implements UrlVerifier {
  private static final Logger LOGGER = LoggerFactory.getLogger(GoogleSafeBrowsingUrlVerifier.class);
  private static final String PARAMETER_URL = "url";
  private final GoogleSafeBrowsingConfig config;
  private final RestTemplate client;

  @Autowired
  public GoogleSafeBrowsingUrlVerifier(RestTemplate clnt, GoogleSafeBrowsingConfig cnfg) {
    this.client = clnt;
    this.config = cnfg;
  }

  public final boolean isSafe(String url) {
    final Map<String, String> formParameters = this.config.getParameters();
    formParameters.put(PARAMETER_URL, url);

    boolean safe = true;
    try {
      final ResponseEntity<String> response = client.getForEntity(
          config.getApiUrl(),
          String.class,
          formParameters);
      LOGGER.debug("Google Safe Browsing API returned HTTP Status: {} and message: {}", response.getStatusCode(), response.getBody());

      if (response.getStatusCode() == HttpStatus.OK) {
        LOGGER.debug("Possible unsafe link: {} - {}", url, response.getBody());
        safe = false;
      }

    } catch (RestClientException restException) {
      LOGGER.warn("Exception occurred during HTTP call to Google Safe Browsing API: {}", restException);
    }

    return safe;
  }
}
