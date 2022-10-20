package br.com.frovas.repository;

import javax.enterprise.context.ApplicationScoped;

import br.com.frovas.model.Movie;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class MovieRepository implements PanacheRepository<Movie>{

}
