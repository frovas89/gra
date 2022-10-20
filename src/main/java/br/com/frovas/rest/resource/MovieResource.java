package br.com.frovas.rest.resource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.com.frovas.repository.MovieRepository;

@Path("/movies")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MovieResource {

	private MovieRepository repository;

	@Inject
	public MovieResource(MovieRepository repository) {
		this.repository = repository;
	}

}
