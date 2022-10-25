package br.com.frovas.repository;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.NoResultException;

import br.com.frovas.model.Movie;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class MovieRepository implements PanacheRepository<Movie>{


	public Movie findByTitle(String title) {
		PanacheQuery<Movie> query = find("title", title);
		Movie movie = null;
		try {
			movie = query.singleResult();
		} catch  (NoResultException nre) {
			return null;
		}
		return movie;
	}

	public List<Movie> listWinners() {
		PanacheQuery<Movie> query = find("winner", Boolean.TRUE);
		List<Movie> listMovies = query.list();
		return listMovies;
	}


}
