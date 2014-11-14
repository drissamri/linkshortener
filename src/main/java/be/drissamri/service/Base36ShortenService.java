package be.drissamri.service;

import be.drissamri.service.exception.InvalidURLException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static be.drissamri.service.SupportedProtocol.HTTP;
import static be.drissamri.service.SupportedProtocol.HTTPS;

@Service
public class Base36ShortenService implements URLShortenService {
  private static final int RADIX = 36;
  private static final String PIPE = "-";
  private static final String HTTP_PROTOCOL = "http://";
  private static final String HTTPS_PROTOCOL = "https://";

  @Override
  public String shorten(String url) {
    return encode(url);
  }

  private String encode(String url) {
    if (StringUtils.isEmpty(url)) {
      throw new InvalidURLException();
    }

    boolean isSupportedProtocol = SupportedProtocol.contains(url);
    if (!isSupportedProtocol) {
      throw new InvalidURLException();
    }

    String hexValue = Integer.toString(url.hashCode(), RADIX);
    if (hexValue.startsWith(PIPE)) {
      hexValue = hexValue.substring(1);
    }

    // TODO: Implement database check to prevent collisions
    return hexValue;
  }

}