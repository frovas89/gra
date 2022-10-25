package br.com.frovas.rest.resource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;

import br.com.frovas.GRAController;
import br.com.frovas.model.Movie;
import br.com.frovas.model.Producer;
import br.com.frovas.model.Studio;
import br.com.frovas.repository.MovieRepository;
import br.com.frovas.repository.ProducerRepository;
import br.com.frovas.repository.StudioRepository;
import br.com.frovas.rest.dto.MovieRequestDTO;
import br.com.frovas.rest.dto.ProducerRequestDTO;
import br.com.frovas.rest.dto.StudioRequestDTO;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

@Path("/movies")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MovieResource {
	private MovieRepository repository;
	private ProducerRepository producerRepository;
	private StudioRepository studioRepository;

	@Inject
	public MovieResource(MovieRepository repository, ProducerRepository producerRepository, StudioRepository studioRepository) {
		this.repository = repository;
		this.producerRepository = producerRepository;
		this.studioRepository = studioRepository;
	}


	@GET
	@Path("min-max-winners")
	public Response listMinMaxWinners(@PathParam("title") String title) {

		var list = GRAController.listMinMaxWinners(repository);

		return Response.ok(list).build();
	}

	@GET
	@Path("winners")
	public Response listMinMaxWinners() {

		var list = repository.listWinners();

		return Response.ok(list).build();
	}

	@GET
	public Response listAllMovies() {
		PanacheQuery<Movie> query = repository.findAll();

		return Response.ok(query.list()).build();
	}

	@POST
	@Transactional
	public Response createMovie( MovieRequestDTO dtoRequest) {

		if(dtoRequest.getProducers() == null || dtoRequest.getProducers().isEmpty()) {
			return Response.status(Response.Status.BAD_REQUEST).entity("O Filme deve conter ao menos um Produtor!").build();
		} else {
			for (ProducerRequestDTO dto: dtoRequest.getProducers()) {
				if(StringUtils.isBlank(dto.getName())) {
					return Response.status(Response.Status.BAD_REQUEST).entity("O nome do Produtor não pode estar em branco!").build();
				}
			}
		}

		if(dtoRequest.getStudios() == null || dtoRequest.getStudios().isEmpty()) {
			return Response.status(Response.Status.BAD_REQUEST).entity("O Filme deve conter ao menos um Estúdio!").build();
		} else {
			for (StudioRequestDTO dto: dtoRequest.getStudios()) {
				if(StringUtils.isBlank(dto.getName())) {
					return Response.status(Response.Status.BAD_REQUEST).entity("O nome do Estúdio não pode estar em branco!").build();
				}
			}
		}

		if(dtoRequest.getStudios().isEmpty()) {
			return Response.status(Response.Status.BAD_REQUEST).entity("O Filme deve conter ao menos um Estúdio!").build();
		}

		if(StringUtils.isBlank(dtoRequest.getTitle())) {
			return Response.status(Response.Status.BAD_REQUEST).entity("O título do filme é obrigatório!").build();
		}

		if(dtoRequest.getYear() == null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("O ano do filme é obrigatório!").build();
		}

		if(repository.findByTitle(dtoRequest.getTitle()) != null) {
			return Response.status(Response.Status.CONFLICT).entity("Filme já cadastrado!").build();
		}
//		validar campos notnull: title, year

		List<Producer> listProd = new ArrayList<>();
		for (ProducerRequestDTO dto: dtoRequest.getProducers()) {
			Producer prod = producerRepository.findByName(dto.getName());
			if(prod == null) {
				prod = new Producer();
				prod.setName(dto.getName());
			}
			listProd.add(prod);
		}

		List<Studio> listStu = new ArrayList<>();
		for (StudioRequestDTO dto: dtoRequest.getStudios()) {
			Studio stu = studioRepository.findByName(dto.getName());
			if(stu == null) {
				stu = new Studio();
				stu.setName(dto.getName());
			}
			listStu.add(stu);
		}

		Movie mov = new Movie();
		mov.setYear(dtoRequest.getYear());
		mov.setTitle(dtoRequest.getTitle());
		mov.setStudios(listStu);
		mov.setProducers(listProd);
		mov.setWinner(dtoRequest.getWinner());

		repository.persist(mov);

		return Response.status(Response.Status.CREATED.getStatusCode()).entity(mov).build();
	}





}
