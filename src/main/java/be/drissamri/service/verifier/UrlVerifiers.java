package be.drissamri.service.verifier;

import be.drissamri.service.exception.LinkshortenerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@Component
public class UrlVerifiers {
  private static final String ENCODING_UTF8 = "UTF-8";
  private List<UrlVerifier> urlVerifiers;

  @Autowired
  public UrlVerifiers(List<UrlVerifier> urlVerifiers) {
    this.urlVerifiers = urlVerifiers;
  }

  public boolean isSafe(String url) {
    boolean isValid = true;

    String encodedUrl;
    try {
      encodedUrl = URLEncoder.encode(url, ENCODING_UTF8);
    } catch (UnsupportedEncodingException e) {
      throw new LinkshortenerException("Unable to encode to UTF-8", e);
    }

    for (UrlVerifier verifier : urlVerifiers) {
      if (verifier != null) {

        boolean isValidByProvider = verifier.isSafe(encodedUrl);
        if (!isValidByProvider) {
          isValid = false;
        }
      }
    }

    return isValid;
  }
}