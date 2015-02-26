package be.drissamri.service;

import be.drissamri.LinkEntityBuilder;
import be.drissamri.entity.LinkEntity;
import be.drissamri.repository.LinkRepository;
import be.drissamri.service.exception.InvalidURLException;
import be.drissamri.service.exception.LinkNotFoundException;
import be.drissamri.service.impl.LinkServiceImpl;
import be.drissamri.service.verifier.UrlVerifiers;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;

public class LinkServiceImplTest {
  private static final String HASH = "AZERTY";
  private static final String LONG_URL = "http://www.drissamri.be";
  private static final String LONG_PHISHING_URL = "http://www.drissamri.be/phishing";

  private LinkService linkService;
  @Mock
  private LinkRepository linkRepository;
  @Mock
  private HashService shortenService;
  @Mock
  private UrlVerifiers urlVerifiers;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    linkService = new LinkServiceImpl(shortenService, urlVerifiers, linkRepository);
  }

  @Test
  public void findShouldReturnLinks() {
    Page<LinkEntity> links = new PageImpl<>(Lists.newArrayList(new LinkEntity(), new LinkEntity()));
    given(linkRepository.findAll(any(Pageable.class))).willReturn(links);

    List<LinkEntity> result = linkService.find(1, 10);

    assertThat(result).hasSize(2);
  }

  @Test
  public void deleteShouldSucceed() {
    LinkEntity existingLink = new LinkEntityBuilder().hash(HASH).url(LONG_URL).build();
    given(linkRepository.findByHash(HASH)).willReturn(existingLink);

    linkService.deleteByHash(HASH);

    Mockito.verify(linkRepository, times(1)).delete(existingLink);
  }

  @Test(expected = LinkNotFoundException.class)
  public void deleteUnknownLinkShouldThrowError() {
    given(linkRepository.findByHash(HASH)).willReturn(null);

    linkService.deleteByHash(HASH);
  }

  @Test
  public void createShouldReturnCreatedLink() {
    LinkEntity link = new LinkEntityBuilder().hash(HASH).url(LONG_URL).build();
    given(linkRepository.save(link)).willReturn(link);
    given(shortenService.shorten(LONG_URL)).willReturn(HASH);
    given(urlVerifiers.isSafe(LONG_URL)).willReturn(true);

    LinkEntity result = linkService.create(LONG_URL);

    assertThat(result).isNotNull();
    assertThat(result.getUrl()).isEqualTo(LONG_URL);
    assertThat(result.getHash()).isEqualTo(HASH);
  }

  @Test
  public void shouldReturnExistingHashForExistingLink() {
    LinkEntity link = new LinkEntityBuilder().hash(HASH).url(LONG_URL).build();
    given(linkRepository.findByUrl(LONG_URL)).willReturn(link);
    given(urlVerifiers.isSafe(LONG_URL)).willReturn(true);

    LinkEntity result = linkService.create(LONG_URL);

    assertThat(result.getHash()).isEqualTo(HASH);
  }

  @Test(expected = InvalidURLException.class)
  public void shouldThrowExceptionWhenUnsecureLinkIsProcessed() {
    LinkEntity link = new LinkEntityBuilder().hash(HASH).url(LONG_URL).build();

    given(linkRepository.save(link)).willReturn(link);
    given(shortenService.shorten(LONG_URL)).willReturn(HASH);
    given(urlVerifiers.isSafe(LONG_PHISHING_URL)).willReturn(false);

    linkService.create(LONG_URL);
  }

  @Test
  public void shouldReturnNullWhenHashNotFound() {
    given(linkRepository.findByHash(HASH)).willReturn(null);

    String result = linkService.findUrlByHash(HASH);

    assertThat(result).isNull();
  }

  @Test
  public void shouldReturnUrlForExistingHash() {
    LinkEntity link = new LinkEntityBuilder().hash(HASH).url(LONG_URL).build();
    given(linkRepository.findByHash(HASH)).willReturn(link);

    String result = linkService.findUrlByHash(HASH);

    assertThat(result).isEqualTo(LONG_URL);
  }
}