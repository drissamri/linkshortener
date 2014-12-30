package be.drissamri.rest;

import be.drissamri.entity.LinkEntity;
import be.drissamri.rest.resource.CollectionResource;
import be.drissamri.rest.resource.LinkResource;
import be.drissamri.rest.resource.Resource;
import be.drissamri.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Path(LinkController.LINKS)
public class LinkController {
  public static final String LINKS = "/links";
  private LinkService linkService;

  @Autowired
  public LinkController(LinkService linkService) {
    this.linkService = linkService;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response find(@Context UriInfo uriInfo,
                       @QueryParam("expand") @DefaultValue("false") boolean expand,
                       @QueryParam("offset") @DefaultValue("1")     int offset,
                       @QueryParam("limit")  @DefaultValue("10")    int limit) {
    List<LinkEntity> foundLinks = linkService.find(offset, limit);

    List<Resource> resources = foundLinks
      .stream()
      .map(link -> expand ? new LinkResource(uriInfo, link) : new Resource(uriInfo, link.getHash()))
      .collect(Collectors.toList());

    CollectionResource collection = new CollectionResource(uriInfo, resources, offset, limit);
    return Response.ok().entity(collection).build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public Response createShortLink(@Context UriInfo uriInfo,
                                  @FormParam("url") String url) {
    LinkEntity savedLink = linkService.create(url);

    LinkResource link = new LinkResource(uriInfo, savedLink);
    return created(link);
  }

  @DELETE
  @Path("/{hash}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteLinkByHash(@PathParam(value = "hash") String hash) {
    linkService.deleteByHash(hash);

    return Response.noContent().build();
  }

  private Response created(Resource createdResource) {
    String href = (String) createdResource.get("href");
    URI uri = URI.create(href);
    return Response.created(uri).entity(createdResource).build();
  }

}