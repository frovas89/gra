package br.com.frovas.rest.resource;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.frovas.model.Producer;
import br.com.frovas.repository.ProducerRepository;
import br.com.frovas.rest.dto.ProducerRequestDTO;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

@Path("/producers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProducerResource {

	private ProducerRepository repository;

	@Inject
	public ProducerResource(ProducerRepository repository) {
		this.repository = repository;
	}

	@POST
	@Transactional
	public Response createUser( ProducerRequestDTO dtoRequest) {

		Producer producer = new Producer();
		producer.setName(dtoRequest.getName());
		repository.persist(producer);

		return Response.status(Response.Status.CREATED.getStatusCode()).entity(producer).build();
	}

	@GET
	public Response listAllProducers() {

		PanacheQuery<Producer> query = repository.findAll();

		return Response.ok(query.list()).build();
	}
}
