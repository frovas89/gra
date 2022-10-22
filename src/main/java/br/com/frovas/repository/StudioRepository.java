package br.com.frovas.repository;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.NoResultException;

import br.com.frovas.model.Studio;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class StudioRepository implements PanacheRepository<Studio>{

	public Studio findByName(String name) {
		PanacheQuery<Studio> query = find("name", name);
		Studio studio = null;
		try {
			studio = query.singleResult();
		} catch  (NoResultException nre) {
			return null;
		}
		return studio;
	}

}
