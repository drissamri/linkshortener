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

import be.drissamri.config.safebrowsing.phishtank.PhishTankCondition;
import be.drissamri.config.safebrowsing.phishtank.PhishTankConfig;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Validate URL with the PhishTank API.
 * @author Driss Amri (drissamri@gmail.com)
 * @version $Id$
 */
@Component
@Conditional(PhishTankCondition.class)
public class PhishTankUrlVerifier implements UrlVerifier {
  private static final Logger LOGGER = LoggerFactory.getLogger(GoogleSafeBrowsingUrlVerifier.class);
  private static final String PARAMETER_URL = "url";
  private static final String IN_DATABASE_PARSE_EXPRESSION = "results.in_database";
  private final RestTemplate client;
  private final PhishTankConfig config;

  @Autowired
  public PhishTankUrlVerifier(final RestTemplate clnt, final PhishTankConfig cnfg) {
    this.client = clnt;
    this.config = cnfg;
  }

  @Override
  public final boolean isSafe(final String url) {
    boolean safe = true;
    try {
      final MultiValueMap<String, String> formParameters = this.config.getParameters();
      formParameters.add(PARAMETER_URL, url);
      final ResponseEntity<String> response = this.client.postForEntity(this.config.getApiUrl(), formParameters, String.class);
      if (response.getStatusCode() == HttpStatus.OK) {
        final boolean phish = JsonPath.parse(response.getBody()).read(IN_DATABASE_PARSE_EXPRESSION);
        if (phish) {
          LOGGER.warn("Possible malicious link posted: {}", url);
          LOGGER.debug("PhishTank response: {}", response);
          safe = false;
        }
      } else {
        LOGGER.warn("Request for PhishTank API failed with status: {} - : {}", response.getStatusCode(), response);
      }
    } catch (final RestClientException | PathNotFoundException exception) {
      LOGGER.warn("Something went wrong while processing PhishTank API: {}", exception);
    }
    return safe;
  }
}
