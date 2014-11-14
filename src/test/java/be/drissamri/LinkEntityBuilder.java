package be.drissamri;

import be.drissamri.entity.LinkEntity;

public class LinkEntityBuilder {
  private LinkEntity link;

  public LinkEntityBuilder() {
    link = new LinkEntity();
  }

  public LinkEntityBuilder hash(String hash) {
    this.link.setHash(hash);
    return this;
  }

  public LinkEntityBuilder url(String url) {
    this.link.setUrl(url);
    return this;
  }

  public LinkEntity build() {
    return link;
  }
}
