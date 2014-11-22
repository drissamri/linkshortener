package be.drissamri.service;

import be.drissamri.LinkEntityBuilder;
import be.drissamri.entity.LinkEntity;
import be.drissamri.repository.LinkRepository;
import be.drissamri.service.exception.InvalidURLException;
import be.drissamri.service.exception.LinkNotFoundException;
import be.drissamri.service.impl.LinkServiceImpl;
import be.drissamri.service.verifier.UrlVerifierProviders;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
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
  private UrlVerifierProviders urlVerifierProviders;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    linkService = new LinkServiceImpl(shortenService, urlVerifierProviders, linkRepository);
  }

  @Test
  public void findShouldReturnLinks() {
    given(linkRepository.findAll()).willReturn(Lists.newArrayList(new LinkEntity(), new LinkEntity()));

    List<LinkEntity> links = linkService.find();

    assertThat(links).hasSize(2);
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
    given(urlVerifierProviders.isSafe(LONG_URL)).willReturn(true);

    LinkEntity result = linkService.create(LONG_URL);

    assertThat(result).isNotNull();
    assertThat(result.getUrl()).isEqualTo(LONG_URL);
    assertThat(result.getHash()).isEqualTo(HASH);
  }

  @Test
  public void shouldReturnExistingHashForExistingLink() {
    LinkEntity link = new LinkEntityBuilder().hash(HASH).url(LONG_URL).build();
    given(linkRepository.findByUrl(LONG_URL)).willReturn(link);
    given(urlVerifierProviders.isSafe(LONG_URL)).willReturn(true);

    LinkEntity result = linkService.create(LONG_URL);

    assertThat(result.getHash()).isEqualTo(HASH);
  }

  @Test(expected = InvalidURLException.class)
  public void shouldThrowExceptionWhenUnsecureLinkIsProcessed() {
    LinkEntity link = new LinkEntityBuilder().hash(HASH).url(LONG_URL).build();

    given(linkRepository.save(link)).willReturn(link);
    given(shortenService.shorten(LONG_URL)).willReturn(HASH);
    given(urlVerifierProviders.isSafe(LONG_PHISHING_URL)).willReturn(false);

    linkService.create(LONG_URL);
  }

  @Test
  public void shouldReturnPersonalHomepageWhenHashNotFound() {
    given(linkRepository.findByHash(HASH)).willReturn(null);

    String result = linkService.findUrlByHash(HASH);

    assertThat(result).isEqualTo("http://www.drissamri.be");
  }

  @Test
  public void shouldReturnUrlForExistingHash() {
    LinkEntity link = new LinkEntityBuilder().hash(HASH).url(LONG_URL).build();
    given(linkRepository.findByHash(HASH)).willReturn(link);

    String result = linkService.findUrlByHash(HASH);

    assertThat(result).isEqualTo(LONG_URL);
  }
}