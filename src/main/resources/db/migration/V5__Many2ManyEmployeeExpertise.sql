create table if not exists employees_expertise(
	expertise_id int,
	employee_id int,
	primary key(expertise_id,employee_id),
	foreign key (expertise_id) references expertises(expertise_id),
	foreign key (employee_id) references employees(employee_id)
);