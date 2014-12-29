package be.drissamri.it;

import be.drissamri.Application;
import be.drissamri.entity.LinkEntity;
import be.drissamri.rest.LinkController;
import com.jayway.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static com.jayway.restassured.RestAssured.basic;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class LinkControllerIT {
  private static final String LONG_URL = "http://www.drissamri.be";
  private static final String PARAMETER_HASH = "hash";
  private static final String PARAMETER_URL = "url";

  @Value("${local.server.port}")
  private int port;

  @Before
  public void setUp() {
    RestAssured.authentication = basic("admin", "secret");
    RestAssured.port = port;
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
      .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
      .queryParam(PARAMETER_HASH, savedLink.getHash())
    .delete(LinkController.LINKS)
    .then()
      .statusCode(HttpStatus.NO_CONTENT.value());
     // @formatter:off
  }

  @Test
  public void shouldReturnOKWhenRedirectToUnknownHash() {
    // @formatter:off
    LinkEntity savedLink = createLink(LONG_URL);

    given()
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .pathParam(PARAMETER_HASH, savedLink.getHash())
    .get("/{hash}")
    .then()
      .statusCode(HttpStatus.OK.value());
     // @formatter:off
  }

  private LinkEntity createLink(String url) {
    // @formatter:off
    return given()
            .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .formParameter(PARAMETER_URL, url)
          .post(LinkController.LINKS)
          .then()
            .statusCode(HttpStatus.OK.value())
            .body(PARAMETER_URL, is(url))
            .body(PARAMETER_HASH, not(empty()))
            .extract().as(LinkEntity.class);
     // @formatter:on
  }
}