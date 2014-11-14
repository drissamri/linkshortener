package be.drissamri.service;

import be.drissamri.service.exception.InvalidURLException;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Base36ShortenServiceTest {
  private Base36ShortenService base36ShortenService = new Base36ShortenService();

  @Test
  public void validHttpUrlShouldReturnAHash() {
    String result = base36ShortenService.shorten("http://www.drissamri.be");

    assertThat(result).isEqualTo("miufvo");
  }

  @Test
  public void validHttpsUrlShouldReturnAHash() {
    String result = base36ShortenService.shorten("https://www.drissamri.be");

    assertThat(result).isEqualTo("3yq02h");
  }

  @Test(expected = InvalidURLException.class)
  public void ftpUrlShouldThrowInvalidLinkException() {
    base36ShortenService.shorten("ftp://drissamri.be");
  }

  @Test(expected = InvalidURLException.class)
  public void noProtocolSpecifiedShouldThrowInvalidLinkException() {
    base36ShortenService.shorten("www.drissamri.be");
  }

  @Test(expected = InvalidURLException.class)
  public void nullParameterShouldThrowInvalidLinkException() {
    base36ShortenService.shorten(null);
  }

}