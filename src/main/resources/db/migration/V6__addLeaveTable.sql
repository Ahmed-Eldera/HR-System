create table if not exists leaves (
	leave_id int primary key auto_increment,
	employee_id int not null,
	leave_date date not null,
    created_at DATE DEFAULT CURRENT_DATE,
    foreign key (employee_id) references employees(employee_id)
);
