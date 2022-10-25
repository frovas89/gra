package br.com.frovas;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import br.com.frovas.model.Movie;
import br.com.frovas.model.Producer;
import br.com.frovas.model.Studio;
import br.com.frovas.repository.MovieRepository;
import br.com.frovas.rest.dto.MinMaxIntervalRequestDTO;
import br.com.frovas.rest.dto.ProducerIntervalDTO;
import br.com.frovas.rest.dto.ProducerMovieDTO;

public class GRAController {


	public void teste() {

		//		var movies = em.persist(em);;
		//		movies.forEach(System.out::println);
	}


	public static List<Movie> readCSVFileToObject(FileReader fileReader) throws IOException, CsvException {

		//		List<Movie> movies = new CsvToBeanBuilder<Movie>(fileReader)
		//                .withType(Movie.class).withSeparator(';').withSkipLines(1)
		//                .build()
		//                .parse();

		//		movies.forEach(System.out::println);

		CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build();
		CSVReader reader = new CSVReaderBuilder(fileReader)
				.withCSVParser(csvParser)
				.withSkipLines(1)
				.build();
		List<String[]> records = reader.readAll();

		Iterator<String[]> iterator = records.iterator();

		List<Movie> movies = new ArrayList<Movie>();
		while (iterator.hasNext()) {
			String[] record = iterator.next();
			Movie movie = new Movie();
			movie.setYear(Integer.valueOf(record[0]));
			movie.setTitle(record[1]);

			String[] studios_rec = record[2].split(",");

			List<Studio> studios = new ArrayList<Studio>();
			for (int i = 0; i < studios_rec.length; i++) {

				Studio studio = new Studio();
				studio.setName(studios_rec[i].trim());
				studios.add(studio);
			}

			movie.setStudios(studios);

			String[] producers_rec = record[3].split(",");

			List<Producer> producers = new ArrayList<Producer>();
			for (int i = 0; i < producers_rec.length; i++) {


				String[] producers_rec_and = producers_rec[i].split(" and ");
				if(producers_rec_and.length > 1) {
					for (int j = 0; j < producers_rec_and.length; j++) {
						Producer producer = new Producer();
						producer.setName(producers_rec_and[j].trim());
						producers.add(producer);
					}
				} else {
					Producer producer = new Producer();
					producer.setName(producers_rec[i].trim());
					producers.add(producer);
				}
			}
			movie.setProducers(producers);

			if(record[4].equals("yes")) {
				movie.setWinner(Boolean.TRUE);
			} else {
				movie.setWinner(Boolean.FALSE);
			}

			movies.add(movie);
		}


		reader.close();

		return movies;


	}

	public static MinMaxIntervalRequestDTO listMinMaxWinners(MovieRepository movieRepository) {



		List<ProducerMovieDTO> listProducerDTO = new ArrayList<ProducerMovieDTO>();

		List<Movie> listWinnerMovies = movieRepository.listWinners();


		for (Movie movie : listWinnerMovies) {
			for (Producer producer: movie.getProducers()) {
				ProducerMovieDTO dto = new ProducerMovieDTO();
				dto.setProducerName(producer.getName());
				dto.setMovie(movie.getTitle());
				dto.setYear(movie.getYear());

//				if(!listProducerDTO.contains(dto)) {
					listProducerDTO.add(dto);
//				}
			}
		}

		//		fazer uma HASH onde produtor é a chave?
		HashMap<String, List<ProducerMovieDTO>> hashProducerDTO = new HashMap<String, List<ProducerMovieDTO>>();

		for (ProducerMovieDTO dto : listProducerDTO) {
			if(!hashProducerDTO.containsKey(dto.getProducerName())) {
				List<ProducerMovieDTO> listAux = new ArrayList<ProducerMovieDTO>();
				listAux.add(dto);
				hashProducerDTO.put(dto.getProducerName(), listAux);
			} else {
				hashProducerDTO.get(dto.getProducerName()).add(dto);
			}
		}

		//Hash somente com produtores com mais de 2 filmes vencedores
		HashMap<String, List<ProducerMovieDTO>> hashProducerWinnerDTO = new HashMap<String, List<ProducerMovieDTO>>();
		for (String producerName : hashProducerDTO.keySet()) {
			if(hashProducerDTO.get(producerName).size() > 1) {
				hashProducerWinnerDTO.put(producerName, hashProducerDTO.get(producerName));
			}
		}

		ProducerIntervalDTO dtoMin = new ProducerIntervalDTO();
		dtoMin.setInterval(9999);
		ProducerIntervalDTO dtoMax = new ProducerIntervalDTO();
		dtoMax.setInterval(0);


		List<ProducerIntervalDTO> max = new ArrayList<ProducerIntervalDTO>();
		List<ProducerIntervalDTO> min = new ArrayList<ProducerIntervalDTO>();
		for (String producerName : hashProducerWinnerDTO.keySet()) {

			List<ProducerMovieDTO> listProducersDTO = new ArrayList<>();
			listProducersDTO.addAll(hashProducerDTO.get(producerName));


			//ordena por ano
			Comparator<ProducerMovieDTO> comp = new Comparator<ProducerMovieDTO>() {
				@Override
				public int compare(ProducerMovieDTO dto1, ProducerMovieDTO dto2) {
					return dto1.getYear().compareTo(dto2.getYear());
				}
			};
			Collections.sort(listProducersDTO,  comp);

			// o máximo de diferença de anos será o primeiro ano da lista deste produtor com o último
			Integer firstYear = listProducersDTO.get(0).getYear();
			Integer lastYear = listProducersDTO.get(listProducersDTO.size()-1).getYear();

			//quando é maior, zera a lista e começa adicionar de novo
			if(lastYear - firstYear > dtoMax.getInterval()) {
				dtoMax.setProducer(producerName);
				dtoMax.setInterval(lastYear - firstYear);
				dtoMax.setPreviousWin(firstYear);
				dtoMax.setFollowingWin(lastYear);
				max = new ArrayList<ProducerIntervalDTO>();
				max.add(dtoMax);
			} else if(lastYear - firstYear == dtoMax.getInterval()) {
				//se for igual adiciona na lista (caso de 2 produtores com mesma diferença de anos vencedores)
				//se for menor não faz nada
				max.add(dtoMax);
			}

			int count = 0;

			ProducerIntervalDTO dtoMinAux = new ProducerIntervalDTO();
			// percorrer comparando dois a dois
			while(count < listProducersDTO.size()-1) {

				Integer interval =  listProducersDTO.get(count+1).getYear() - listProducersDTO.get(count).getYear();
				if(interval > 0 && interval < dtoMin.getInterval()) {
					dtoMinAux.setProducer(producerName);
					dtoMinAux.setInterval(lastYear - firstYear);
					dtoMinAux.setPreviousWin(firstYear);
					dtoMinAux.setFollowingWin(lastYear);
				}
				count++;
			}

			if(dtoMinAux.getInterval() != null) {
				if(dtoMinAux.getInterval() < dtoMin.getInterval()) {
					dtoMin = dtoMinAux;
					min = new ArrayList<ProducerIntervalDTO>();
					min.add(dtoMin);
				} else if (dtoMinAux.getInterval() == dtoMin.getInterval()) {
					min.add(dtoMin);
				}
			}
		}

		MinMaxIntervalRequestDTO finalDTO = new MinMaxIntervalRequestDTO();
		finalDTO.setMin(min);
		finalDTO.setMax(max);

		return finalDTO;
	}

}
