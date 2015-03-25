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

package be.drissamri.service.verifier;

import be.drissamri.config.safebrowsing.ApplicationSettings;
import be.drissamri.config.safebrowsing.google.GoogleApiSettings;
import be.drissamri.config.safebrowsing.google.GoogleSafeBrowsingConfig;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.BDDMockito.given;
import org.mockito.Matchers;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class GoogleSafeBrowsingUrlVerifierTest {
    private GoogleSafeBrowsingUrlVerifier verifier;
    @Mock
    private RestTemplate restTemplate;
    private static final String LONG_URL = "http://www.drissamri.be";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        GoogleApiSettings google = new GoogleApiSettings("apiUrl", "apiVersion", "apiKey");
        ApplicationSettings app = new ApplicationSettings("appName", "appVersion");
        verifier = new GoogleSafeBrowsingUrlVerifier(restTemplate,
            new GoogleSafeBrowsingConfig(google, app));
    }

    @Test
    public void shouldReturnTrueForSafeUrl() {
        ResponseEntity<String> verifierResult = new ResponseEntity<>("", HttpStatus.NO_CONTENT);
        given(restTemplate.getForEntity(anyString(), eq(String.class),
            Matchers.<Map<String, String>>any())).willReturn(verifierResult);

        boolean isSafe = verifier.isSafe(LONG_URL);

        assertThat(isSafe).isTrue();
    }

    @Test
    public void shouldReturnFalseForStatus200() {
        ResponseEntity<String> verifierResult = new ResponseEntity<>("", HttpStatus.OK);
        given(restTemplate.getForEntity(
            anyString(),
            eq(String.class),
            Matchers.<Map<String, String>>any()))
        .willReturn(verifierResult);

        boolean safe = verifier.isSafe(LONG_URL);

        assertThat(safe).isFalse();
    }

    @Test
    public void shouldReturnTrueWhenUnableToConnect() {
        given(restTemplate.getForEntity(
            anyString(),
            eq(String.class),
            Matchers.<Map<String, String>>any()))
        .willThrow(RestClientException.class);

        boolean isSafe = verifier.isSafe(LONG_URL);

        assertThat(isSafe).isTrue();
    }
}
