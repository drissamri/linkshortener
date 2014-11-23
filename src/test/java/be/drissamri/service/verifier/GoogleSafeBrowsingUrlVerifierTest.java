package be.drissamri.service.verifier;


import be.drissamri.config.GoogleSafeBrowsingConfig;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.*;

public class GoogleSafeBrowsingUrlVerifierTest {
  private GoogleSafeBrowsingUrlVerifier googleSafeBrowsingUrlVerifier;
  @Mock
  private RestTemplate restTemplate;
  private static final String LONG_URL = "http://www.drissamri.be";

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    googleSafeBrowsingUrlVerifier = new GoogleSafeBrowsingUrlVerifier(restTemplate, new GoogleSafeBrowsingConfig("apiKey", "apiUrl", "apiVersion", "appName", "appVersion"));
  }

  @Test
  public void shouldReturnTrueForSafeUrl() {
    ResponseEntity<String> verifierResult = new ResponseEntity<>("", HttpStatus.NO_CONTENT);
    given(restTemplate.getForEntity(anyString(), eq(String.class), anyMap())).willReturn(verifierResult);

    boolean isSafe = googleSafeBrowsingUrlVerifier.isSafe(LONG_URL);

    assertThat(isSafe).isTrue();
  }

  @Test
  public void shouldReturnFalseForStatus200() {
    ResponseEntity<String> verifierResult = new ResponseEntity<>("", HttpStatus.OK);
    given(restTemplate.getForEntity(anyString(), eq(String.class), anyMap())).willReturn(verifierResult);

    boolean isSafe = googleSafeBrowsingUrlVerifier.isSafe(LONG_URL);

    assertThat(isSafe).isFalse();
  }

  @Test
  public void shouldReturnTrueWhenUnableToConnect() {
    given(restTemplate.getForEntity(anyString(), eq(String.class), anyMap())).willThrow(RestClientException.class);

    boolean isSafe = googleSafeBrowsingUrlVerifier.isSafe(LONG_URL);

    assertThat(isSafe).isTrue();
  }
}