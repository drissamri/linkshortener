package be.drissamri.it;

import be.drissamri.LinkshortenerApp;
import be.drissamri.entity.LinkEntity;
import be.drissamri.repository.LinkRepository;
import be.drissamri.rest.LinkController;
import com.jayway.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.preemptive;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LinkshortenerApp.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class LinkControllerIT {
  private static final String LONG_URL = "http://www.drissamri.be";
  private static final String HASH = "AZERTY";

  @Autowired
  private LinkRepository linkRepository;
  @Value("${local.server.port}")
  private int port;

  @Before
  public void setUp() {
  /*
    LinkEntity link = new LinkEntity();
    link.setUrl(LONG_URL);
    link.setHash(HASH);

    linkRepository.deleteAll();
    linkRepository.save(Lists.newArrayList(link));*/

    RestAssured.port = port;
    RestAssured.authentication = preemptive().basic("admin", "pass");
  }

  @Test
  public void shouldReturnAuthenticationExceptionWhenAccessingWithoutCredentials() {
    // @formatter:off
    given()
      .auth().basic("","")
      .contentType(MediaType.APPLICATION_JSON_VALUE)
    .get(LinkController.LINKS)
    .then()
      .statusCode(HttpStatus.UNAUTHORIZED.value());
      // @formatter:on
  }

  @Test
  public void shouldReturnOKWhenRetrievingAllLinks() {
    // @formatter:off
    createLink(LONG_URL);

    given()
      .contentType(MediaType.APPLICATION_JSON_VALUE)
    .get(LinkController.LINKS)
    .then()
      .statusCode(HttpStatus.OK.value());
     // @formatter:on

  }

  @Test
  public void shouldReturnOKNoContentWhenCreatingLinkAndDeleteAfterward() {
    // @formatter:off
    LinkEntity savedLink = createLink(LONG_URL);

    given()
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .queryParam("hash", savedLink.getHash())
    .delete(LinkController.LINKS)
    .then()
      .statusCode(HttpStatus.NO_CONTENT.value());
     // @formatter:off
  }

  private LinkEntity createLink(String url) {
    // @formatter:off
    return given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("url", url)
          .post(LinkController.LINKS)
          .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .body("url", is(url))
            .body("hash", not(empty()))
            .extract().as(LinkEntity.class);
        // @formatter:on
  }

}