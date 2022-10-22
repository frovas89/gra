package br.com.frovas.rest.dto;

import java.util.List;

import lombok.Data;

@Data
public class FinalProducerIntervalDTO {

	private List<ProducerIntervalDTO> min;
	private List<ProducerIntervalDTO> max;
}
