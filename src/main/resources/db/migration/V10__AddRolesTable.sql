create table roles (
    id int primary key auto_increament,
    name varchar(255) unique not null
);
alter table employee add column role_id int default "EMPLOYEE";