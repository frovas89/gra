package br.com.frovas.rest.resource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.frovas.GRAController;
import br.com.frovas.model.Movie;
import br.com.frovas.repository.MovieRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

@Path("/movies")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MovieResource {
	private MovieRepository repository;

	@Inject
	public MovieResource(MovieRepository repository) {
		this.repository = repository;
	}

	@GET
	public Response listAllMovies() {
		PanacheQuery<Movie> query = repository.findAll();

		return Response.ok(query.list()).build();
	}

	@GET
	@Path("min-max-winners")
	public Response listMinMaxWinners(@PathParam("title") String title) {


		var list = GRAController.listMinMaxWinners(repository);

		return Response.ok(list).build();
	}

	//	@GET
	//	@Path("load-movies")
	//	@Transactional
	//	public void saveMovies() {
	//
	//
	//		try {
	//			List<Movie> movies = GRAController.readCSVFileToObject(new FileReader("movielist.csv"));
	//
	//			System.out.println("Lista de filmes");
	//			for (Movie movie : movies) {
	//				System.out.println(movie.toString());
	//			}
	//
	//			for (Movie movie : movies) {
	//
	//
	//				if(repository.findByTitle(movie.getTitle()) == null) {
	//
	//					int indexP = 0;
	//					for (br.com.frovas.model.Producer producer : movie.getProducers()) {
	//
	//						br.com.frovas.model.Producer prod = producerRepository.findByName(producer.getName());
	//						if(prod != null) {
	//							movie.getProducers().set(indexP, prod);
	//						}
	//						indexP ++;
	//					}
	//
	//					int indexS = 0;
	//					for (br.com.frovas.model.Studio studio : movie.getStudios()) {
	//
	//						br.com.frovas.model.Studio stu = studioRepository.findByName(studio.getName());
	//						if(stu != null) {
	//							movie.getStudios().set(indexS, stu);
	//						}
	//						indexS ++;
	//					}
	//
	//					repository.persist(movie);
	//				}
	//
	//			}
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//		}
	//
	//
	//	}
}
