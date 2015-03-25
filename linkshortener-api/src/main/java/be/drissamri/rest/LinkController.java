/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Driss Amri
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package be.drissamri.rest;

import be.drissamri.entity.LinkEntity;
import be.drissamri.rest.resource.CollectionResource;
import be.drissamri.rest.resource.LinkResource;
import be.drissamri.rest.resource.Resource;
import be.drissamri.service.LinkService;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path(LinkController.LINKS)
public class LinkController {
  private static final Logger LOGGER = LoggerFactory.getLogger(LinkController.class);
  public static final String LINKS = "/links";
  private final LinkService service;

  @Autowired
  public LinkController(LinkService srvc) {
    this.service = srvc;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public final Response find(
    @Context UriInfo context,
    @QueryParam("expand") @DefaultValue("false") final boolean expand,
    @QueryParam("offset") @DefaultValue("1") final int offset,
    @QueryParam("limit") @DefaultValue("10") final int limit) throws Exception {
    LOGGER.debug("Find links request. expand: {} - offset: {}, limit: {}", expand, offset, limit);

    final List<LinkEntity> foundLinks = service.find(offset, limit);

    final List<Resource> resources = foundLinks
      .stream()
      .map(link -> expand ? new LinkResource(context, link) : new Resource(context, link.getHash()))
      .collect(Collectors.toList());

    final CollectionResource collection = new CollectionResource(context, resources, offset, limit);
    LOGGER.debug("Returning collection of {} links", collection.get("size"));
    return Response.ok().entity(collection).build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public final Response createShortLink(@Context UriInfo context, @FormParam("url") String url) {
    LOGGER.debug("Shorten request for: {}", url);
    final LinkResource link = new LinkResource(context, this.service.create(url));
    LOGGER.debug("Returning link: {}", link);
    return created(link);
  }

  @DELETE
  @Path("/{hash}")
  @Produces(MediaType.APPLICATION_JSON)
  public final Response deleteLinkByHash(@PathParam(value = "hash") String hash) {
    LOGGER.info("Request link delete for hash: {}", hash);
    this.service.deleteByHash(hash);
    return Response.noContent().build();
  }

  private Response created(Resource createdResource) {
    final String href = (String) createdResource.get("href");
    return Response
      .created(URI.create(href))
      .entity(createdResource)
      .build();
  }
}
