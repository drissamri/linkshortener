package be.drissamri.controller;

import be.drissamri.model.Link;
import be.drissamri.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LinkController {
  private LinkService linkService;

  @Autowired
  public LinkController(LinkService linkService) {
    this.linkService = linkService;
  }

  @RequestMapping("/links")
  public Link createLink(@RequestParam(value = "longUrl") String longUrl) {
    return linkService.createLink(longUrl);
  }
}
