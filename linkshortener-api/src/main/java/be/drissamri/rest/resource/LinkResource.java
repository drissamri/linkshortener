package be.drissamri.rest.resource;

import be.drissamri.entity.LinkEntity;

import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class LinkResource extends Resource {
  public LinkResource(UriInfo uriInfo, LinkEntity link) {
    super(uriInfo, link.getHash());

    put("url", link.getUrl());
    put("hash", link.getHash());
    put("shortUrl", buildUrl(uriInfo, link.getHash()));
  }

  private String buildUrl(UriInfo uriInfo, String hash) {
    URI absolutePath = uriInfo.getAbsolutePath();
    StringBuilder urlBuilder = new StringBuilder();
    urlBuilder.append(absolutePath.getScheme());
    urlBuilder.append("://");
    urlBuilder.append(absolutePath.getHost());

    int port = absolutePath.getPort();
    if (port != -1) {
      urlBuilder.append(":");
      urlBuilder.append(absolutePath.getPort());
    }

    urlBuilder.append("/");
    urlBuilder.append(hash);

    return urlBuilder.toString();
  }
}
