create table if not exists employees (
	employee_id int primary key ,
	name varchar(255) ,
	date_of_birth date,
	gender ENUM('MALE','FEMALE'),
	graduation_date date,
	salary float(2),
	department_id int,
	foreign key (department_id) references departments(department_id),
--	manager_id int,
--	foreign key (manager_id) references employees(employee_id),
	team_id int,
	foreign key (team_id) references teams(team_id)
);