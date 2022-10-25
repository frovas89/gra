package br.com.frovas.rest.resource;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.frovas.model.Studio;
import br.com.frovas.repository.StudioRepository;
import br.com.frovas.rest.dto.StudioRequestDTO;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

@Path("/Studios")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StudioResource {

	private StudioRepository repository;

	@Inject
	public StudioResource(StudioRepository repository) {
		this.repository = repository;
	}

	@POST
	@Transactional
	public Response createStudio( StudioRequestDTO dtoRequest) {

		Studio studio = new Studio();
		studio.setName(dtoRequest.getName());
		repository.persist(studio);

		return Response.status(Response.Status.CREATED.getStatusCode()).entity(studio).build();
	}

	@GET
	public Response listAllStudios() {

		PanacheQuery<Studio> query = repository.findAll();

		return Response.ok(query.list()).build();
	}

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteStudio( @PathParam("id") Long id) {
       Studio stu = repository.findById(id);

        if(stu != null) {
            repository.delete(stu);
            return Response.noContent().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();

    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response updateStudio(@PathParam("id") Long id, StudioRequestDTO dtoRequest) {
        Studio stu = repository.findById(id);

        if(stu != null) {
            stu.setName(dtoRequest.getName());
            return Response.noContent().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
