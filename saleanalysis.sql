CREATE TABLE sales (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(100) NOT NULL,
    sale_date DATE NOT NULL,
    sale_amount DECIMAL(10, 2) NOT NULL
);

INSERT INTO sales (product_name, sale_date, sale_amount) VALUES
    ('Product A', '2023-07-01', 100.00),
    ('Product B', '2023-07-02', 150.00),
    ('Product A', '2023-08-01', 200.00),
    ('Product C', '2023-08-02', 120.00),
    ('Product B', '2023-08-10', 180.00);
    INSERT INTO sales (product_name, sale_date, sale_amount) VALUES
       ('labtop', '2023-07-01', 100.00),
    ('pc', '2023-07-02', 150.00),
    ('toys', '2023-08-01', 200.00),
    ('mobile', '2023-08-02', 120.00),
    ('Product B', '2023-08-10', 180.00);