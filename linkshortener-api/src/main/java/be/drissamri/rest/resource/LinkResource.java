package be.drissamri.rest.resource;

import be.drissamri.entity.LinkEntity;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class LinkResource extends Resource {
    public LinkResource(UriInfo uri, LinkEntity link) {
        super(uri, link.getHash());

        put("url", link.getUrl());
        put("hash", link.getHash());
        put("shortUrl", buildUrl(uri, link.getHash()));
    }

    private String buildUrl(UriInfo context, String hash) {
        final URI uri = context.getAbsolutePath();
        final StringBuilder url = new StringBuilder();
        url.append(uri.getScheme());
        url.append("://");
        url.append(uri.getHost());

        final int port = uri.getPort();
        if (port != -1) {
            url.append(":");
            url.append(uri.getPort());
        }

        url.append(uri.getPath().substring(0, uri.getPath().lastIndexOf('/') + 1));
        url.append(hash);

        return url.toString();
    }
}
