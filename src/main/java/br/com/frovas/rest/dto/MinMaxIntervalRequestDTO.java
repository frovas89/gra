package br.com.frovas.rest.dto;

import java.util.List;

import lombok.Data;

@Data
public class MinMaxIntervalRequestDTO {

	private List<ProducerIntervalDTO> min;
	private List<ProducerIntervalDTO> max;
}
