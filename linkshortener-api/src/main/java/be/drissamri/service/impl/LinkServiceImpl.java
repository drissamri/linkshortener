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
package be.drissamri.service.impl;

import be.drissamri.entity.LinkEntity;
import be.drissamri.repository.LinkRepository;
import be.drissamri.service.HashService;
import be.drissamri.service.LinkService;
import be.drissamri.service.exception.InvalidURLException;
import be.drissamri.service.exception.LinkNotFoundException;
import be.drissamri.service.verifier.UrlVerifiers;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LinkServiceImpl implements LinkService {
  private static final Logger LOGGER = LoggerFactory.getLogger(LinkServiceImpl.class);
  private static final String URL_ENCODE_REGEX = "[a-zA-Z0-9_~-]+$";
  private LinkRepository linkRepository;
  private HashService shortenService;
  private UrlVerifiers verifiers;

  @Autowired
  public LinkServiceImpl(HashService shortenService, UrlVerifiers verifiers, LinkRepository linkRepository) {
    this.linkRepository = linkRepository;
    this.shortenService = shortenService;
    this.verifiers = verifiers;
  }

  @Override
  @Transactional(readOnly = true)
  public List<LinkEntity> find(int offset, int limit) {
    int page = (int) Math.ceil(offset / limit);
    Pageable pageRequest = new PageRequest(page, limit);
    Page<LinkEntity> foundLinks = linkRepository.findAll(pageRequest);

    LOGGER.debug("Found {} links", foundLinks.getTotalElements());
    return foundLinks.getContent();
  }

  @Override
  @Transactional
  public LinkEntity create(String url) {
    LOGGER.debug("Request to shorten: {}", url);
    LinkEntity resultLink;

    boolean isSafeUrl = verifiers.isSafe(url);
    if (isSafeUrl) {
      LinkEntity existingLink = linkRepository.findByUrl(url);
      if (existingLink != null) {
        LOGGER.debug("URL {} already exists in database: {}", url, existingLink);
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
    LOGGER.info("Delete request for link with hash: ", hash);

    LinkEntity foundLink = linkRepository.findByHash(hash);
    if (foundLink == null) {
      throw new LinkNotFoundException("No link found for hash: " + hash);
    }

    linkRepository.delete(foundLink);
    LOGGER.info("Deleted: {}", foundLink);
  }

  @Override
  public String findUrlByHash(String hash) {
    String url = null;

    LOGGER.trace("Retrieving link for the hash: ", hash);
    LinkEntity foundLink = linkRepository.findByHash(hash);
    if (foundLink == null) {
      LOGGER.info("No link found for hash: {}", hash);
    } else {
      LOGGER.debug("Found link corresponding to the hash: {} is {}", foundLink);
      url = foundLink.getUrl();
    }

    return url;
  }

  private LinkEntity createAndSaveLink(String url) {
    final String hash = shortenService.shorten(url);
    LinkEntity link = linkRepository.save(
      new LinkEntity(url, hash));

    LOGGER.debug("Successfully created new link: {}", link);
    return link;
  }
}
