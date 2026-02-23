alter table employees
add column YOE int not null default 0;

alter table employees
add column created_at TIMESTAMP not null default CURRENT_TIMESTAMP;
