package be.drissamri.rest;


import be.drissamri.entity.LinkEntity;
import be.drissamri.service.LinkService;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.ArrayList;

import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.assertj.core.api.Assertions.assertThat;

@Ignore
public class LinkControllerTest {
  private LinkController linkController;
  @Mock
  private LinkService linkService;

  @Before
  public void setupMockMvc() {
    MockitoAnnotations.initMocks(this);
    linkController = new LinkController(linkService);
    RestAssuredMockMvc.standaloneSetup(linkController);
  }

  @Test
  public void find2LinksWithDefaultPaging() {
    ArrayList<LinkEntity> links = Lists.newArrayList(new LinkEntity(), new LinkEntity());
    Mockito.when(linkService.find()).thenReturn(links);

    // @formatter:off
    LinkEntity[] result = given()
      .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when()
    .get(LinkController.LINKS)
    .then()
      .statusCode(HttpStatus.OK.value())
      .extract().as(LinkEntity[].class);
    // @formatter:on

    assertThat(result).hasSize(2);
  }
}