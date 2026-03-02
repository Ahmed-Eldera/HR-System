create table if not exists expertises(
	expertise_id int primary key auto_increment,
	name varchar(255) unique not null
);