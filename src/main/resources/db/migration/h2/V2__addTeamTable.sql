create table if not exists teams (
	team_id int primary key auto_increment,
	name varchar(255) not null
);
alter table teams add unique (name);