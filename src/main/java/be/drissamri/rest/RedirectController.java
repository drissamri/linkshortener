package be.drissamri.rest;

import be.drissamri.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.net.URI;

@Component
@Path("/")
public class RedirectController {
  private LinkService linkService;

  @Autowired
  public RedirectController(LinkService linkService) {
    this.linkService = linkService;
  }

  @GET
  @Path("/{hash}")
  public Response redirect(@PathParam("hash") String hash) {
    Response response;

    String url = linkService.findUrlByHash(hash);
    if (!StringUtils.isEmpty(url)) {
      URI uri = URI.create(url);
      response = Response.status(Response.Status.MOVED_PERMANENTLY).location(uri).build();
    } else {
      response = Response.status(Response.Status.BAD_REQUEST).build();
    }

    return response;
  }
}
