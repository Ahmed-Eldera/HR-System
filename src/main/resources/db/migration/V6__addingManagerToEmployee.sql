alter table employees add manager_id int;
alter table employees add constraint fk_const foreign key (manager_id) references employees(employee_id);