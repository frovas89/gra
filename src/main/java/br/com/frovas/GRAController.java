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
import br.com.frovas.rest.dto.FinalProducerIntervalDTO;
import br.com.frovas.rest.dto.ProducerDTO;
import br.com.frovas.rest.dto.ProducerIntervalDTO;

public class GRAController {


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

	public static FinalProducerIntervalDTO listMinMaxWinners() {


		//		obter somente produtores vencedores e jogar numa DTO
		//		arrumar a DTO com produtor, filme e ano que venceu
		//		ver como fazer isso direto na ProducerController

		List<ProducerDTO> listProducerDTO = new ArrayList<ProducerDTO>();





		//		List<Movie> listWinnerMovies = MovieController.listWinnerMovies();








		try {
			List<Movie> listWinnerMovies = readCSVFileToObject(new FileReader("movielist.csv"));


			for (Movie movie : listWinnerMovies) {
				ProducerDTO dto = new ProducerDTO();
				for (Producer producer: movie.getProducers()) {
					dto.setProducerName(producer.getName());
					dto.setMovie(movie.getTitle());
					dto.setYear(movie.getYear());

					listProducerDTO.add(dto);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}


		//		fazer uma HASH onde produtor é a chave?
		HashMap<String, List<ProducerDTO>> hashProducerDTO = new HashMap<String, List<ProducerDTO>>();

		for (ProducerDTO dto : listProducerDTO) {
			if(!hashProducerDTO.containsKey(dto.getProducerName())) {
				List<ProducerDTO> listAux = new ArrayList<ProducerDTO>();
				listAux.add(dto);
				hashProducerDTO.put(dto.getProducerName(), listAux);
			} else {
				hashProducerDTO.get(dto.getProducerName()).add(dto);
			}
		}

		//		percorrer a lista por produtor, vendo a diferença de anos utilizando duas DTOs auxiliares, máxima e minima
		//		substituindo a medida que encontrar outro

		ProducerIntervalDTO dtoMin = new ProducerIntervalDTO();
		dtoMin.setInterval(9999);
		ProducerIntervalDTO dtoMax = new ProducerIntervalDTO();
		dtoMax.setInterval(0);

		for (String producerName : hashProducerDTO.keySet()) {

			List<ProducerDTO> listProducersDTO = new ArrayList<>();
			listProducersDTO.addAll(hashProducerDTO.get(producerName));
			//se o produtor tem só um filme vencedor, nem precisa comparar
			if(listProducersDTO.size() > 1) {

				Comparator<ProducerDTO> comp = new Comparator<ProducerDTO>() {
					@Override
					public int compare(ProducerDTO dto1, ProducerDTO dto2) {
						return dto1.getYear().compareTo(dto2.getYear());
					}
				};
				Collections.sort(listProducersDTO,  comp);
				// o máximo de diferença de anos será o primeiro da lista com o último
				Integer firstYear = listProducersDTO.get(0).getYear();
				Integer lastYear = listProducersDTO.get(listProducersDTO.size()-1).getYear();
				if(lastYear - firstYear > dtoMax.getInterval()) {
					dtoMax.setProducer(producerName);
					dtoMax.setInterval(lastYear - firstYear);
					dtoMax.setPreviousWin(firstYear);
					dtoMax.setFollowingWin(lastYear);
				}

				int count = 0;
				// percorrer comparando dois a dois
				while(count < listProducersDTO.size()-2) {

					Integer interval =  listProducersDTO.get(count+1).getYear() - listProducersDTO.get(count).getYear();
					if(interval > 0 && interval < dtoMin.getInterval()) {
						dtoMin.setProducer(producerName);
						dtoMin.setInterval(lastYear - firstYear);
						dtoMin.setPreviousWin(firstYear);
						dtoMin.setFollowingWin(lastYear);
					}
					count++;
				}
			}
		}

		FinalProducerIntervalDTO finalDTO = new FinalProducerIntervalDTO();
		List<ProducerIntervalDTO> min = new ArrayList<ProducerIntervalDTO>();
		min.add(dtoMin);
		List<ProducerIntervalDTO> max = new ArrayList<ProducerIntervalDTO>();
		max.add(dtoMax);
		finalDTO.setMin(min);
		finalDTO.setMax(max);

		return finalDTO;
	}

}
