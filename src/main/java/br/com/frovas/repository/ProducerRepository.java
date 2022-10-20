package br.com.frovas.repository;

import javax.enterprise.context.ApplicationScoped;

import br.com.frovas.model.Producer;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class ProducerRepository implements PanacheRepository<Producer>{

}
