package be.drissamri.service.impl;

import be.drissamri.entity.LinkEntity;
import be.drissamri.repository.LinkRepository;
import be.drissamri.service.HashService;
import be.drissamri.service.LinkService;
import be.drissamri.service.exception.InvalidURLException;
import be.drissamri.service.exception.LinkNotFoundException;
import be.drissamri.service.verifier.UrlVerifiers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LinkServiceImpl implements LinkService {
  private static final Logger logger = LoggerFactory.getLogger(LinkServiceImpl.class);
  private static final String URL_ENCODE_REGEX = "[a-zA-Z0-9_~-]+$";
  private LinkRepository linkRepository;
  private HashService shortenService;
  private UrlVerifiers urlVerifiers;

  @Autowired
  public LinkServiceImpl(HashService shortenService, UrlVerifiers urlVerifiers, LinkRepository linkRepository) {
    this.linkRepository = linkRepository;
    this.shortenService = shortenService;
    this.urlVerifiers = urlVerifiers;
  }

  @Override
  @Transactional(readOnly = true)
  public List<LinkEntity> find(int offset, int limit) {
    int page = (int) Math.ceil(offset / limit);
    Pageable pageRequest = new PageRequest(page, limit);
    Page<LinkEntity> foundLinks = linkRepository.findAll(pageRequest);

    logger.debug("Found {} links", foundLinks.getTotalElements());
    return foundLinks.getContent();
  }

  @Override
  @Transactional
  public LinkEntity create(String url) {
    logger.debug("Request to shorten: {}", url);
    LinkEntity resultLink;

    boolean isSafeUrl = urlVerifiers.isSafe(url);
    if (isSafeUrl) {
      LinkEntity existingLink = linkRepository.findByUrl(url);
      if (existingLink != null) {
        logger.debug("URL {} already exists in database: {}", url, existingLink);
        resultLink = existingLink;
      } else {
        resultLink = createAndSaveLink(url);
      }
      return resultLink;
    } else {
      throw new InvalidURLException("URL " + url + " might pose a security risk, so we won't process it");
    }
  }

  @Override
  @Transactional
  public void deleteByHash(String hash) {
    logger.info("Delete request for link with hash: ", hash);

    LinkEntity foundLink = linkRepository.findByHash(hash);
    if (foundLink == null) {
      throw new LinkNotFoundException("No link found for hash: " + hash);
    }

    linkRepository.delete(foundLink);
    logger.info("Deleted: {}", foundLink);
  }

  @Override
  public String findUrlByHash(String hash) {
    String url = null;

    logger.trace("Retrieving link for the hash: ", hash);
    LinkEntity foundLink = linkRepository.findByHash(hash);
    if (foundLink == null) {
      logger.info("No link found for hash: {}", hash);
    } else {
      logger.debug("Found link corresponding to the hash: {} is {}", foundLink);
      url = foundLink.getUrl();
    }

    return url;
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