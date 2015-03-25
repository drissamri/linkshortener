package be.drissamri.it;

import be.drissamri.Application;
import be.drissamri.rest.LinkController;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;
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
import static javax.ws.rs.core.Response.Status.*;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.empty;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class LinkControllerIT {
    private static final String LONG_URL = "http://www.drissamri.be";
    private static final String PARAMETER_HASH = "hash";
    private static final String PARAMETER_URL = "url";
    public static final String API_V1 = "/api/v1";

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
            .get(API_V1 + LinkController.LINKS)
        .then()
            .statusCode(HttpStatus.OK.value());
        // @formatter:on
    }

    @Test
    public void shouldReturnOKNoContentWhenCreatingLinkAndDeleteAfterward() {
        // @formatter:off
        String savedLink = createLink(LONG_URL);

        given()
            .pathParam(PARAMETER_HASH, savedLink)
            .delete(API_V1 + LinkController.LINKS + "/{hash}")
            .then()
            .statusCode(NO_CONTENT.getStatusCode());
        // @formatter:off
    }

    @Test
    public void shouldReturnOKAfterRedirectToKnownHash() {
        // @formatter:off
        String savedLink = createLink(LONG_URL);

        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .pathParam(PARAMETER_HASH, savedLink)
            .get(API_V1 + "/redirect/{hash}")
            .then()
            .statusCode(OK.getStatusCode());
        // @formatter:off
    }

    private String createLink(String url) {
        // @formatter:off
        JsonPath result = given()
            .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .formParameter(PARAMETER_URL, url)
            .post("/api/v1" + LinkController.LINKS)
            .then()
            .statusCode(CREATED.getStatusCode())
            .body(PARAMETER_HASH, not(empty()))
            .extract().body().jsonPath();
        // @formatter:on

        return result.get("hash");
    }
}
