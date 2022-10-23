package br.com.frovas.rest;

import static io.restassured.RestAssured.given;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.frovas.rest.resource.MovieResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
@TestHTTPEndpoint(MovieResource.class)
public class MovieResourceTest {

	@Test
	@DisplayName("deve listar todos os filmes")
	public void listAllUsersTest(){

		given()
			.contentType(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(200)
			.body("size()", Matchers.is(0));
	}
}
