package be.drissamri.service;

import be.drissamri.model.Link;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class LinkServiceImpl implements LinkService {
  private static final Logger LOGGER = LoggerFactory.getLogger(LinkServiceImpl.class);
  private static final String URL_PARAMETER = "URL";
  private String apiUrl;
  private RestTemplate restTemplate;

  @Autowired
  public LinkServiceImpl(RestTemplate restTemplate, @Value("${linkshortener.api.url}") String apiUrl) {
    this.restTemplate = restTemplate;
    this.apiUrl = apiUrl;

    LOGGER.debug("Linkshortener API URL: {}", apiUrl);
  }

  @Override
  public Link createLink(String longUrl) {
    MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
    parameters.add(URL_PARAMETER, longUrl);

    ResponseEntity<Link> response;
    Link result = null;
    try {
      response = restTemplate.postForEntity(apiUrl, parameters, Link.class);

      HttpStatus responseStatusCode = response.getStatusCode();
      if (HttpStatus.CREATED == responseStatusCode || HttpStatus.OK == responseStatusCode) {
        result = response.getBody();
      } else {
        LOGGER.warn("Shorten request returned HTTP status: {}", responseStatusCode);
      }
    } catch (RestClientException ex) {
      throw new RuntimeException(ex);
    }

    return result;
  }
}