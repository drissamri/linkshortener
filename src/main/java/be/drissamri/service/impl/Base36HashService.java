package be.drissamri.service.impl;

import be.drissamri.service.HashService;
import be.drissamri.service.SupportedProtocol;
import be.drissamri.service.exception.InvalidURLException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class Base36HashService implements HashService {
  private static final int RADIX = 36;
  private static final String PIPE = "-";

  @Override
  public String shorten(String url) {
    return encode(url);
  }

  private String encode(String url) {
    if (StringUtils.isEmpty(url)) {
      throw new InvalidURLException("Supplied invalid url: empty");
    }

    boolean isSupportedProtocol = SupportedProtocol.contains(url);
    if (!isSupportedProtocol) {
      throw new InvalidURLException("URL protocol not supported");
    }

    String hexValue = Integer.toString(url.hashCode(), RADIX);
    if (hexValue.startsWith(PIPE)) {
      hexValue = hexValue.substring(1);
    }

    // TODO: Implement database check to prevent collisions
    return hexValue;
  }

}