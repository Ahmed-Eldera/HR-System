CREATE TABLE payments (
    id INT NOT NULL primary key AUTO_INCREMENT,
    amount DOUBLE,
    salary_id INT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (salary_id) REFERENCES employees (employee_id)
)
