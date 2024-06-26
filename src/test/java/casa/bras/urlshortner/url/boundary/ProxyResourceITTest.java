package casa.bras.urlshortner.url.boundary;

import static io.restassured.RestAssured.given;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.config.RedirectConfig;
import io.restassured.config.RestAssuredConfig;
import jakarta.ws.rs.core.HttpHeaders;
import org.junit.jupiter.api.Test;

@QuarkusTest
class ProxyResourceITTest extends ProxyIntegrationTest {

  public static final RestAssuredConfig REST_ASSURED_CONFIG =
      RestAssuredConfig.config().redirect(RedirectConfig.redirectConfig().followRedirects(false));

  @Test
  void configuredProxy_hash_returnsRedirectToProxyUrl() {
    String url = "http://localhost";

    String hash = createAndAssertProxy(url).hash();

    given()
        .config(REST_ASSURED_CONFIG)
        .when()
        .get(hash)
        .then()
        .statusCode(301)
        .header(HttpHeaders.LOCATION, url)
        .noRootPath();
  }

  @Test
  void noConfiguredProxy_hash_returnsNotFound() {
    var hash = "1234567";

    given().config(REST_ASSURED_CONFIG).when().get(hash).then().statusCode(404);
  }
}
