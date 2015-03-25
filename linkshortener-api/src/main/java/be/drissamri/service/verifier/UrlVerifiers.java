package be.drissamri.service.verifier;

import be.drissamri.service.exception.LinkshortenerException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UrlVerifiers {
  private static final Logger LOGGER = LoggerFactory.getLogger(UrlVerifier.class);
  private static final String ENCODING_UTF8 = "UTF-8";
  private final List<UrlVerifier> verifiers;

  @Autowired
  public UrlVerifiers(List<UrlVerifier> vrfrs) {
    this.verifiers = vrfrs;
  }

  public boolean isSafe(String url) {
    boolean safe = true;

    final String encodedUrl;
    try {
      encodedUrl = URLEncoder.encode(url, ENCODING_UTF8);
    } catch (UnsupportedEncodingException e) {
      LOGGER.warn("Unable to encode url: {}", url);
      throw new LinkshortenerException("Unable to encode to UTF-8", e);
    }

    for (UrlVerifier verifier : verifiers) {
      if (verifier != null) {
        boolean isValidByProvider = verifier.isSafe(encodedUrl);
        if (!isValidByProvider) {
          safe = false;
          break;
        }
      }
    }

    return safe;
  }
}
