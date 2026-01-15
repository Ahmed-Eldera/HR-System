create table if not exists salaries (
	salary_id int primary key auto_increment,
	employee_id int not null,
    raise_percentage double not null default 0,
    gross_salary double not null,
    net_salary double not null,
    created_at TIMESTAMP not null  DEFAULT CURRENT_TIMESTAMP(),
    foreign key (employee_id) references employees(employee_id)
);

insert into salaries (employee_id,gross_salary,net_salary)
select employee_id,salary,(salary - salary *0.15 - 500) from employees;