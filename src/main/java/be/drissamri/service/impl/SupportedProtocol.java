package be.drissamri.service.impl;

import org.springframework.util.StringUtils;

public enum SupportedProtocol {
  HTTP("http://"),
  HTTPS("https://");

  private String protocol;

  SupportedProtocol(String protocol) {
    this.protocol = protocol;
  }

  public String getProtocol() {
    return protocol;
  }

  public static boolean contains(String protocol) {
    boolean isSupported = false;
    for (SupportedProtocol validProtocol : SupportedProtocol.values()) {
      if (StringUtils.startsWithIgnoreCase(protocol, validProtocol.getProtocol())) {
        isSupported = true;
      }
    }

    return isSupported;
  }
}
