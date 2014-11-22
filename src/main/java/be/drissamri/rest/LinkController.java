package be.drissamri.rest;

import be.drissamri.entity.LinkEntity;
import be.drissamri.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(LinkController.LINKS)
public class LinkController {
  public static final String LINKS = "/links";
  private LinkService linkService;

  @Autowired
  public LinkController(LinkService linkService) {
    this.linkService = linkService;
  }

  @RequestMapping(method = GET, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<List<LinkEntity>> find() {
    List<LinkEntity> links = linkService.find();

    return ResponseEntity.ok().body(links);
  }

  @RequestMapping(method = POST,  consumes = APPLICATION_FORM_URLENCODED_VALUE, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<LinkEntity> createShortLink(@RequestParam("url") String url) {
    LinkEntity savedLink = linkService.create(url);

    return ResponseEntity.ok().body(savedLink);
  }

  @RequestMapping(method = DELETE)
  public ResponseEntity<Void> deleteLinkByHash(@RequestParam(value = "hash") String hash) {
    linkService.deleteByHash(hash);

    return ResponseEntity.noContent().build();
  }
}