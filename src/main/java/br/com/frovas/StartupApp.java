package br.com.frovas;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;

import br.com.frovas.model.Movie;
import br.com.frovas.model.Producer;
import br.com.frovas.model.Studio;
import br.com.frovas.repository.MovieRepository;
import br.com.frovas.repository.ProducerRepository;
import br.com.frovas.repository.StudioRepository;
import io.quarkus.runtime.StartupEvent;

@ApplicationScoped
public class StartupApp {

	@Inject
	MovieRepository movieRepository;
	@Inject
	ProducerRepository producerRepository;
	@Inject
	StudioRepository studioRepository;


	StartupApp(MovieRepository movieRepository, ProducerRepository producerRepository, StudioRepository studioRepository) {
		this.movieRepository = movieRepository;
		this.producerRepository = producerRepository;
		this.studioRepository = studioRepository;

	}

	void onStart(@Observes StartupEvent ev) {
//		loadDataFromCSV("movielist.csv");
	}

	@Transactional
	public void loadDataFromCSV(String file) {
		List<Movie> movies = new ArrayList<>();
		try {
			movies = GRAController.readCSVFileToObject(new FileReader(file));
		} catch (Exception e) {
			e.printStackTrace();
		}

		//			System.out.println("Lista de filmes");
		//			for (Movie movie : movies) {
		//				System.out.println(movie.toString());
		//			}

		for (Movie movie : movies) {


			if(movieRepository.findByTitle(movie.getTitle()) == null) {

				int indexP = 0;
				for (Producer producer : movie.getProducers()) {

					Producer prod = producerRepository.findByName(producer.getName());
					if(prod != null) {
						movie.getProducers().set(indexP, prod);
					}
					indexP ++;
				}

				int indexS = 0;
				for (Studio studio : movie.getStudios()) {

					Studio stu = studioRepository.findByName(studio.getName());
					if(stu != null) {
						movie.getStudios().set(indexS, stu);
					}
					indexS ++;
				}

				movieRepository.persist(movie);
			}

		}



	}



}
