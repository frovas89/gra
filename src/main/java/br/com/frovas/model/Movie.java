package br.com.frovas.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "movies")
@Data
public class Movie {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name="year")
	private Integer year;
	@Column(name="title")
	private String title;
	@ManyToMany
	@JoinTable(
			name = "rl_movie_producer",
			joinColumns = @JoinColumn(name = "id_movie"),
			inverseJoinColumns = @JoinColumn(name = "id_producer"))
	private List<Producer> producers;
	@ManyToMany
	@JoinTable(
			name = "rl_movie_studio",
			joinColumns = @JoinColumn(name = "id_movie"),
			inverseJoinColumns = @JoinColumn(name = "id_studio"))
	private List<Studio> studios;
	@Column(name="winner")
	private Boolean winner;

}