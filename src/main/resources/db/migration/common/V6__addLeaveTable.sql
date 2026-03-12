create table if not exists leaves (
	leave_id int primary key auto_increment,
	employee_id int not null,
	leave_date date not null,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
    foreign key (employee_id) references employees(employee_id)
);
