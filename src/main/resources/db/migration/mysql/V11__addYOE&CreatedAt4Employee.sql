alter table employees
add column YOE int not null default 0
add column created_at TIMESTAMP not null  DEFAULT CURRENT_TIMESTAMP();
