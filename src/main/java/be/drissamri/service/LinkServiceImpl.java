package be.drissamri.service;

import be.drissamri.entity.LinkEntity;
import be.drissamri.repository.LinkRepository;
import be.drissamri.service.exception.LinkNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LinkServiceImpl implements LinkService {
  private Logger logger = LoggerFactory.getLogger(LinkServiceImpl.class);
  private LinkRepository linkRepository;
  private URLShortenService shortenService;

  @Autowired
  public LinkServiceImpl(URLShortenService shortenService, LinkRepository linkRepository) {
    this.linkRepository = linkRepository;
    this.shortenService = shortenService;
  }

  @Override
  @Transactional(readOnly = true)
  public List<LinkEntity> find() {
    List<LinkEntity> foundLinks = linkRepository.findAll();

    logger.trace("Found {} foundLinks", foundLinks.size());
    return foundLinks;
  }

  @Override
  @Transactional
  public LinkEntity create(String url) {
    logger.debug("Request to shorten: {}", url);
    LinkEntity resultLink;

    LinkEntity existingLink = linkRepository.findByUrl(url);
    if (existingLink != null) {
      logger.debug("URL {} already exists in database: {}", url, existingLink);
      resultLink = existingLink;
    } else {
      resultLink = createAndSaveLink(url);

    }

    return resultLink;
  }

  @Override
  @Transactional
  public void deleteByHash(String hash) {
    logger.info("Delete request for link with hash: ", hash);

    LinkEntity foundLink = linkRepository.findByHash(hash);
    if (foundLink == null) {
      throw new LinkNotFoundException();
    }

    linkRepository.delete(foundLink);
    logger.info("Deleted: {}", foundLink);
  }

  private LinkEntity createAndSaveLink(String url) {
    String hash = shortenService.shorten(url);

    LinkEntity requestedLink = new LinkEntity();
    requestedLink.setUrl(url);
    requestedLink.setHash(hash);
    LinkEntity savedLink = linkRepository.save(requestedLink);

    logger.debug("Successfully created new link: {}", savedLink);

    return savedLink;
  }
}