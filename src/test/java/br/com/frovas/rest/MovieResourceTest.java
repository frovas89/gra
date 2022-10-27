package br.com.frovas.rest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.google.gson.Gson;

import br.com.frovas.model.Movie;
import br.com.frovas.model.Producer;
import br.com.frovas.model.Studio;
import br.com.frovas.rest.dto.MinMaxIntervalRequestDTO;
import br.com.frovas.rest.resource.MovieResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
@TestHTTPEndpoint(MovieResource.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MovieResourceTest {

	@TestHTTPResource("/movies/min-max-winners")
	URL apiURL;

	Movie movieWithoutYear;
	Movie movieWithoutProducer;
	Movie sameProducerMovie;
	Movie existentMovie;
	Movie movie;

	@BeforeEach
    public void setUP() {

		Producer sameProducer = new Producer();
		sameProducer.setName("Matthew Vaughn");
		List<Producer> listSameProducer = new ArrayList<>();
		listSameProducer.add(sameProducer);

		Producer existentProducer = new Producer();
		existentProducer.setName("Adam Sandler");
		List<Producer> listProducer = new ArrayList<>();
		listProducer.add(existentProducer);


		Producer prod = new Producer();
		prod.setName("Producer 1");
		List<Producer> listProd = new ArrayList<>();
		listProd.add(prod);

		Studio stu = new Studio();
		stu.setName("Studio 1");
		List<Studio> listStu = new ArrayList<>();
		listStu.add(stu);

		sameProducerMovie = new Movie();
		sameProducerMovie.setTitle("Test1");
		sameProducerMovie.setProducers(listSameProducer);
		sameProducerMovie.setStudios(listStu);
		sameProducerMovie.setWinner(Boolean.TRUE);
		sameProducerMovie.setYear(2028);

		movie = new Movie();
		movie.setTitle("Novo filme mesmo produtor");
		movie.setProducers(listProducer);
		movie.setStudios(listStu);
		movie.setWinner(Boolean.TRUE);
		movie.setYear(1990);

		movieWithoutYear = new Movie();
		movieWithoutYear.setTitle("Movie Title 1");
		movieWithoutYear.setProducers(listProd);
		movieWithoutYear.setStudios(listStu);

		movieWithoutProducer = new Movie();
		movieWithoutProducer.setTitle("Movie Title 2");
		movieWithoutProducer.setStudios(listStu);
		movieWithoutProducer.setYear(2012);

		existentMovie = new Movie();
		existentMovie.setTitle("Can't Stop the Music");
		existentMovie.setProducers(listProd);
		existentMovie.setStudios(listStu);
		existentMovie.setYear(2012);

	}

	@Test
	@DisplayName(" ### deve listar todos os filmes (total 206) ### ")
	@Order(1)
	public void listAllMoviesTest(){

		given()
			.contentType(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(200)
			.body("size()", Matchers.is(206));
	}

	 @Test
	 @DisplayName(" ### deve listar os produtores com o mínimo e o máximo de intervalo de prêmios, sendo o máximo 13 anos e o mínimo 1 ### ")
	 @Order(2)
	 public void listMinMaxWinnersTest() {

        var response =
                given()
                    .contentType(ContentType.JSON)
                .when()
                    .get(apiURL)
                .then()
                    .extract().response();

        var minList = response.jsonPath().getList("min");
        var maxList = response.jsonPath().getList("max");

        assertEquals(Response.Status.OK.getStatusCode(), response.statusCode());
        assertEquals(1, minList.size());
        assertEquals(1, maxList.size());

        MinMaxIntervalRequestDTO body = new Gson().fromJson(response.getBody().asString(), MinMaxIntervalRequestDTO.class);
        assertEquals(body.getMax().get(0).getInterval(), 13);
        assertEquals(body.getMin().get(0).getInterval(), 1);
    }


	@Test
	@DisplayName(" ### deve criar novo filme com produtor existente com sucesso ### ")
	@Order(3)
	public void createMovieTest(){

		var response = given()
			.contentType(ContentType.JSON)
			.body(sameProducerMovie)
		.when()
			.post()
		.then()
			.extract().response();

		assertEquals(201, response.statusCode());
        assertNotNull(response.jsonPath().getString("id"));
	}

	@Test
    @DisplayName(" ### deve listar os produtores com o mínimo e o máximo de intervalo de prêmios, sendo que na lista de máximo tem 2 resultados ### ")
    @Order(4)
    public void listMinMaxWinnersSecondTest() {

        var response =
                given()
                    .contentType(ContentType.JSON)
                .when()
                    .get(apiURL)
                .then()
                    .extract().response();

        var minList = response.jsonPath().getList("min");
        var maxList = response.jsonPath().getList("max");

        assertEquals(Response.Status.OK.getStatusCode(), response.statusCode());
        assertEquals(1, minList.size());
        assertEquals(2, maxList.size());

        MinMaxIntervalRequestDTO body = new Gson().fromJson(response.getBody().asString(), MinMaxIntervalRequestDTO.class);
        assertEquals(body.getMax().get(0).getInterval(), 13);
        assertEquals(body.getMin().get(0).getInterval(), 1);
    }


	@Test
	@DisplayName(" ### deve criar novo filme com produtor existente com sucesso ### ")
	@Order(5)
	public void createMovieSecondTest(){

		var response = given()
			.contentType(ContentType.JSON)
			.body(movie)
		.when()
			.post()
		.then()
			.extract().response();

		assertEquals(201, response.statusCode());
        assertNotNull(response.jsonPath().getString("id"));
	}

    @Test
    @DisplayName(" ### deve listar os produtores com o mínimo e o máximo de intervalo de prêmios, sendo o máximo 21 anos e o mínimo 1 ### ")
    @Order(6)
    public void listMinMaxWinnersThirdTest() {

        var response =
                given()
                    .contentType(ContentType.JSON)
                .when()
                    .get(apiURL)
                .then()
                    .extract().response();

        var minList = response.jsonPath().getList("min");
        var maxList = response.jsonPath().getList("max");

        assertEquals(Response.Status.OK.getStatusCode(), response.statusCode());
        assertEquals(1, minList.size());
        assertEquals(1, maxList.size());

        MinMaxIntervalRequestDTO body = new Gson().fromJson(response.getBody().asString(), MinMaxIntervalRequestDTO.class);
        assertEquals(body.getMax().get(0).getInterval(), 21);
        assertEquals(body.getMin().get(0).getInterval(), 1);
    }

	@Test
	@DisplayName(" ### deve listar todos os filmes (total 208) ### ")
	@Order(7)
	public void listAllMoviesSecondTest(){

		given()
			.contentType(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(200)
			.body("size()", Matchers.is(208));
	}

    @Test
	@DisplayName(" ### deve retornar 400 quando não tem Produtores ### ")
	public void createMovieWithoutProducerTest(){

		given()
			.contentType(ContentType.JSON)
			.body(movieWithoutProducer)
		.when()
			.post()
		.then()
			.statusCode(Response.Status.BAD_REQUEST.getStatusCode())
			.body(Matchers.is("O Filme deve conter ao menos um Produtor!"));
	}

    @Test
	@DisplayName(" ### deve retornar 400 quando não preencheu ano ### ")
	public void createMovieWithoutYearTest(){

		given()
			.contentType(ContentType.JSON)
			.body(movieWithoutYear)
		.when()
			.post()
		.then()
			.statusCode(Response.Status.BAD_REQUEST.getStatusCode())
			.body(Matchers.is("O ano do filme é obrigatório!"));
	}

    @Test
   	@DisplayName(" ### deve retornar 409 quando o Filme já existe ### ")
   	public void createExistentMovieTest(){

   		given()
   			.contentType(ContentType.JSON)
   			.body(existentMovie)
   		.when()
   			.post()
   		.then()
   			.statusCode(Response.Status.CONFLICT.getStatusCode())
   			.body(Matchers.is("Filme já cadastrado!"));
   	}

    //adicionar um novo filme pra ter mais diferença e testar de novo
    //como ver se o intervalo é aquele mesmo? consigo entrar mais na DTO de retorno?
}
