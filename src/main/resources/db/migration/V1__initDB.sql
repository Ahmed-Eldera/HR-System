create table if not exists teams (
	team_id int primary key auto_increment,
	name varchar(255) not null unique
);

create table if not exists departments (
	department_id int primary key not null auto_increment,
	name varchar(255) unique not null
);

create table if not exists employees (
	employee_id int primary key not null,
	name varchar(255) not null,
	date_of_birth date,
	gender ENUM('MALE','FEMALE'),
	graduation_date date,
	salary float(2),
	department_id int not null,
	foreign key (department_id) references departments(department_id),
	manager_id int,
	foreign key (manager_id) references employees(employee_id),
	team_id int,
	foreign key (team_id) references teams(team_id)
);

create table if not exists expertises(
	expertise_id int primary key auto_increment,
	name varchar(255) unique not null
);

create table if not exists employees_expertise(
	expertise_id int,
	employee_id int,
	primary key(expertise_id,employee_id),
	foreign key (expertise_id) references expertises(expertise_id),
	foreign key (employee_id) references employees(employee_id)
);
