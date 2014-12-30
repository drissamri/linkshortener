package be.drissamri.rest.resource;

import javax.ws.rs.core.UriInfo;
import java.util.Collection;
import java.util.Collections;

public class CollectionResource extends Resource {
  private static final int DEFAULT_LIMIT = 10;
  private static final int DEFAULT_OFFSET = 1;

  public CollectionResource(UriInfo info, Collection resources) {
    this(info, resources, DEFAULT_OFFSET, getLimit(resources));
  }

  public CollectionResource(UriInfo uriInfo, Collection resources, int offset, int limit) {
    super(uriInfo);

    put("offset", offset);
    put("limit", limit);
    put("size", resources.size());
    put("items", resources != null ? resources : Collections.emptyList());
  }

  private static int getLimit(Collection c) {
    return getLimit(c != null ? c.size() : 0);
  }

  private static int getLimit(int limit) {
    return Math.max(DEFAULT_LIMIT, limit);
  }

}
