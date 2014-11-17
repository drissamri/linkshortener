package be.drissamri.rest;

import be.drissamri.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class RedirectController {
  private LinkService linkService;

  @Autowired
  public RedirectController(LinkService linkService) {
    this.linkService = linkService;
  }


  @RequestMapping(value = "{hash}", method = RequestMethod.GET)
  public String redirect(@PathVariable("hash") String hash) {
    return "redirect:" + linkService.findUrlByHash(hash);
  }
}
