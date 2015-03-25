package be.drissamri.rest.resource;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.LinkedHashMap;

public class Resource extends LinkedHashMap<String, Object> {
  public Resource(UriInfo uriInfo, String id) {
    UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
    UriBuilder path = uriBuilder.path(id);
    URI uri = path.build();

    put("href", uri.toString());
  }

  public Resource(UriInfo info) {
    put("href", info.getAbsolutePath().toString());
  }

}
