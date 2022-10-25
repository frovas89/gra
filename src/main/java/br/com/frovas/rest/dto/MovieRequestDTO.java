package br.com.frovas.rest.dto;

import java.util.List;

import lombok.Data;

@Data
public class MovieRequestDTO {

	private Integer year;
	private String title;
	private List<ProducerRequestDTO> producers;
	private List<StudioRequestDTO> studios;
	private Boolean winner;
}
