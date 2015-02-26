package be.drissamri.model;

import java.util.Objects;

public class Link {
  private String url;
  private String hash;
  private String shortUrl;

  public Link(String shortUrl, String url, String hash) {
    this.shortUrl = shortUrl;
    this.url = url;
    this.hash = hash;
  }

  public Link() {

  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getShortUrl() {
    return shortUrl;
  }

  public void setShortUrl(String shortUrl) {
    this.shortUrl = shortUrl;
  }

  public String getHash() {
    return hash;
  }

  public void setHash(String hash) {
    this.hash = hash;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(url);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final Link other = (Link) obj;
    return Objects.equals(this.url, other.url) && Objects.equals(this.shortUrl, other.shortUrl);
  }
}
