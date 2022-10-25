package br.com.frovas.rest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProducerMovieDTO {

	private String producerName;
	private String movie;
	private Integer year;

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ProducerMovieDTO other = (ProducerMovieDTO) obj;
		if (producerName == null) {
			if (other.producerName != null) {
				return false;
			}
		} else if (movie == null) {
			if (other.movie != null) {
				return false;
			}
		} else if (!producerName.equals(other.producerName) || !movie.equals(other.movie)) {
			return false;
		}
		return true;
	}
}
