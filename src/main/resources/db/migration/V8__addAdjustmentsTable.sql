create table if not exists salary_adjustments(
	adjustment_id int primary key auto_increment,
	employee_id int,
    amount double not null,
    adjustment_date date not null,
    foreign key (employee_id) references employees(employee_id)
);