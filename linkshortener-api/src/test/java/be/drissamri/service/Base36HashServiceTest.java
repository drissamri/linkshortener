package be.drissamri.service;

import be.drissamri.service.exception.InvalidURLException;
import be.drissamri.service.impl.Base36HashService;
import org.junit.Ignore;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Ignore
public class Base36HashServiceTest {
  private Base36HashService base36ShortenService = new Base36HashService();

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
