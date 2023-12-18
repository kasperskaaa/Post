CREATE TABLE users
(
    id         SERIAL PRIMARY KEY,
    email      varchar(50),
    name       varchar(50) NOT NULL,
    surname    varchar(50) NOT NULL,
    patronymic varchar(50) NOT NULL,
    phone      varchar(25) NOT NULL UNIQUE
);

CREATE TABLE departments
(
    id      SERIAL PRIMARY KEY,
    title   varchar(100) NOT NULL,
    address varchar(150) NOT NULL
);

CREATE TABLE employees
(
    id                 SERIAL PRIMARY KEY,
    email              varchar(50),
    name               varchar(50) NOT NULL,
    surname            varchar(50) NOT NULL,
    patronymic         varchar(50) NOT NULL,
    phone              varchar(25) NOT NULL UNIQUE,
    experience         int,
    hired              date        NOT NULL,
    fired              date,
    work_department_id int         NOT NULL REFERENCES departments (id)
);

CREATE TABLE orders
(
    id                      SERIAL PRIMARY KEY,
    title                   varchar(150)   NOT NULL,
    type                    varchar(50)    NOT NULL,
    parcel_price            decimal(10, 2) NOT NULL,
    delivery_price          decimal(10, 2) NOT NULL,
    total_price             decimal(10, 2) NOT NULL,
    sender_id               int            NOT NULL REFERENCES users (id),
    recipient_id            int            NOT NULL REFERENCES users (id),
    department_sender_id    int            NOT NULL REFERENCES departments (id),
    department_recipient_id int            NOT NULL REFERENCES departments (id),
    employee_id             int            NOT NULL REFERENCES employees (id),
    status                  int    NOT NULL
);

INSERT INTO users (email, name, surname, patronymic, phone)
VALUES
    ('user1@email.com', 'Grace', 'Wilson', 'M.', '0660632562'),
    ('user2@email.com', 'Daniel', 'Taylor', 'K.', '0932255035'),
    ('user3@email.com', 'Sophia', 'Johnson', 'A.', '0965835284');

INSERT INTO departments (title, address)
VALUES
    ('Department #1', '123 Elm St, CityG'),
    ('Department #2', '456 Oak St, CityH'),
    ('Department #3', '789 Pine St, CityI');

INSERT INTO employees (email, name, surname, patronymic, phone, experience, hired, work_department_id)
VALUES
    ('emp1@email.com', 'Liam', 'Brown', 'A.', '0660632562', 4, '2022-03-10', 1),
    ('emp2@email.com', 'Olivia', 'Miller', 'J.', '0932255035', 2, '2023-01-15', 2),
    ('emp3@email.com', 'Ethan', 'Martinez', 'T.', '0965835284', 5, '2021-08-20', 3);

INSERT INTO orders (title, type, parcel_price, delivery_price, total_price, sender_id, recipient_id, department_sender_id, department_recipient_id, employee_id, status)
VALUES
    ('Order 7', 'Standard', 15.00, 8.00, 23.00, 1, 2, 1, 2, 1, 0),
    ('Package A', 'Express', 30.00, 12.00, 42.00, 3, 1, 3, 1, 2, 0),
    ('Sample Order', 'Standard', 10.00, 5.00, 15.00, 2, 3, 2, 2, 3, 0);