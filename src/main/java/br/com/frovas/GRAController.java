package br.com.frovas;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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

}
