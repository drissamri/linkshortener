package be.drissamri.service.verifier;

import be.drissamri.config.safebrowsing.phishtank.PhishTankConfig;
import be.drissamri.config.safebrowsing.phishtank.PhishTankSettings;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.*;

public class PhishTankUrlVerifierTest {
  private PhishTankUrlVerifier phishTankUrlVerifier;
  @Mock
  private RestTemplate restTemplate;
  private static final String LONG_URL = "http://www.drissamri.be";
  private static final String JSON_PHISH_RESULT = "{ 'results': { 'in_database': true } }";
  private static final String JSON_SAFE_RESULT = "{ 'results': { 'in_database': false } }";
  private static final String JSON_UNKOWN_RESULT = "{ }";

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    phishTankUrlVerifier = new PhishTankUrlVerifier(
        restTemplate,
        new PhishTankConfig(
            new PhishTankSettings("", "")
        ));
  }

  @Test
  public void shouldReturnFalseForPhishingLink() {
    ResponseEntity<String> verifierResult = new ResponseEntity<>(JSON_PHISH_RESULT, HttpStatus.OK);
    given(restTemplate.postForEntity(anyString(), any(LinkedMultiValueMap.class), eq(String.class))).willReturn(verifierResult);

    boolean result = phishTankUrlVerifier.isSafe(LONG_URL);

    assertThat(result).isFalse();
  }

  @Test
  public void shouldReturnTrueForSafeLink() {
    ResponseEntity<String> verifierResult = new ResponseEntity<>(JSON_SAFE_RESULT, HttpStatus.OK);
    given(restTemplate.postForEntity(anyString(), any(LinkedMultiValueMap.class), eq(String.class))).willReturn(verifierResult);

    boolean result = phishTankUrlVerifier.isSafe(LONG_URL);

    assertThat(result).isTrue();
  }

  @Test
  public void shouldReturnTrueWhenExceedingApiLimit() {
    ResponseEntity<String> verifierResult = new ResponseEntity<>(JSON_SAFE_RESULT, HttpStatus.BANDWIDTH_LIMIT_EXCEEDED);
    given(restTemplate.postForEntity(anyString(), any(LinkedMultiValueMap.class), eq(String.class))).willReturn(verifierResult);

    boolean result = phishTankUrlVerifier.isSafe(LONG_URL);

    assertThat(result).isTrue();
  }

  @Test
  public void shouldReturnTrueForUnknownBody() {
    ResponseEntity<String> verifierResult = new ResponseEntity<>(JSON_UNKOWN_RESULT, HttpStatus.OK);
    given(restTemplate.postForEntity(anyString(), any(LinkedMultiValueMap.class), eq(String.class))).willReturn(verifierResult);

    boolean result = phishTankUrlVerifier.isSafe(LONG_URL);

    assertThat(result).isTrue();
  }
}
