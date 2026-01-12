create table if not exists raises(
	raise_id int primary key auto_increment,
	employee_id int not null,
    raise_percentage double not null,
    salary_after_raise double not null,
    last_raise int,
    created_at TIMESTAMP not null  DEFAULT CURRENT_TIMESTAMP(),
    foreign key (employee_id) references employees(employee_id),
    foreign key (last_raise) references raises(raise_id)
);