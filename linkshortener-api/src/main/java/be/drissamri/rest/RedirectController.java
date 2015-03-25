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
  private LinkService service;

  @Autowired
  public RedirectController(LinkService srvc) {
    this.service = srvc;
  }

  @GET
  @Path("/{hash}")
  public Response redirect(@PathParam("hash") String hash) {
    Response response;

    final String url = service.findUrlByHash(hash);
    if (!StringUtils.isEmpty(url)) {
      URI uri = URI.create(url);
      response = Response
        .status(Response.Status.MOVED_PERMANENTLY)
        .location(uri)
        .build();
    } else {
      response = Response
        .status(Response.Status.BAD_REQUEST)
        .build();
    }

    return response;
  }
}
