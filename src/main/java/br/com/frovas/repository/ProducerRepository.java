package br.com.frovas.repository;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.NoResultException;

import br.com.frovas.model.Producer;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class ProducerRepository implements PanacheRepository<Producer>{

	public Producer findByName(String name) {
		PanacheQuery<Producer> query = find("name", name);
		Producer producer = null;
		try {
			producer = query.singleResult();
		} catch  (NoResultException nre) {
			return null;
		}
		return producer;
	}

}
