package be.drissamri.service;

import be.drissamri.model.Link;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class LinkServiceImpl implements LinkService {
  private RestTemplate restTemplate;

  @Autowired
  public LinkServiceImpl(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @Override
  public Link createLink(String longUrl) {
    RestTemplate restTemplate = new RestTemplate();
    MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
    parameters.add("url", longUrl);

    ResponseEntity<Link> response;
    Link result = null;
    try {
      response = restTemplate.postForEntity(
        "http://localhost:9080/api/v1/links",
        parameters,
        Link.class);

      if (HttpStatus.CREATED == response.getStatusCode() || HttpStatus.OK == response.getStatusCode()) {
        result = response.getBody();
      }

    } catch (RestClientException ex) {
      throw new RuntimeException(ex);
    }

    return result;
  }
}