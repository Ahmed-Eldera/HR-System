create table if not exists employees (
	employee_id int primary key auto_increment,
	name varchar(255) not null ,
	date_of_birth date not null,
    gender ENUM('MALE','FEMALE') not null,
	graduation_date date not null,
	salary float(2) not null,
	department_id int not null,
	foreign key (department_id) references departments(department_id),
	team_id int not null ,
	foreign key (team_id) references teams(team_id)
);
alter table employees add manager_id int;
alter table employees add constraint fk_const foreign key (manager_id) references employees(employee_id);