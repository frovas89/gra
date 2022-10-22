package br.com.frovas.rest.dto;

import lombok.Data;

@Data
public class ProducerIntervalDTO {

	private String producer;
	private Integer interval;
	private Integer previousWin;
	private Integer followingWin;

}
