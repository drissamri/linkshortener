package be.drissamri.rest.resource;

import be.drissamri.entity.LinkEntity;

import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class LinkResource extends Resource {
  public LinkResource(UriInfo uriInfo, LinkEntity link) {
    super(uriInfo, link.getHash());

    put("url", link.getUrl());
    put("hash", link.getHash());

    URI absolutePath = uriInfo.getAbsolutePath();
    String hostUrl = absolutePath.getScheme() + "://" + absolutePath.getHost() + ":" + absolutePath.getPort() + "/";
    put("short_url", hostUrl + link.getHash());

  }
}
