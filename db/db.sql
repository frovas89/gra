create database gra;

create table producers (
	id bigserial not null primary key,
	name varchar(100) not null,
	unique(name)
);
 
create table studios (
	id bigserial not null primary key,
	name varchar(100) not null,
	unique(name)
);

create table movies (
	id bigserial not null primary key, 
	year integer not null,
	title varchar(200) not null, 
	winner boolean,
	unique(title)
);

create table rl_movie_producer (
	id_movie bigint not null, 
	id_producer bigint not null
);

create table rl_movie_studio (
	id_movie bigint not null, 
	id_studio bigint not null
);

alter table rl_movie_producer add constraint id_movie_rl_movie_producer_fk foreign key (id_movie) references movies;
alter table rl_movie_producer add constraint id_producer_rl_movie_producer_fk foreign key (id_producer) references producers;
alter table rl_movie_studio add constraint id_movie_rl_movie_studio_fk foreign key (id_movie) references movies;
alter table rl_movie_studio add constraint id_studio_rl_movie_studio_fk foreign key (id_studio) references studios;