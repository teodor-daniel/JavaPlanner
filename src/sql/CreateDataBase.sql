CREATE TABLE company (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         address VARCHAR(255),
                         registration_number VARCHAR(100),
                         phone VARCHAR(50),
                         email VARCHAR(100)
);

CREATE TABLE employees (
                           id SERIAL PRIMARY KEY,
                           name VARCHAR(255),
                           age INT,
                           phone_number VARCHAR(20) UNIQUE,
                           email VARCHAR(255) UNIQUE,
                           salary DOUBLE PRECISION,
                           department VARCHAR(50),
                           employed_status BOOLEAN,
                           bonus INT DEFAULT NULL,
                           team_lead_id INTEGER NULL,
                           FOREIGN KEY (team_lead_id) REFERENCES employees(id) ON DELETE SET NULL
);

CREATE TABLE projects (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(255),
                          team_lead_id INTEGER REFERENCES employees(id),
                          start_date DATE,
                          end_date DATE,
                          status VARCHAR(50)
);

CREATE TABLE tasks (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(255),
                       description TEXT,
                       assigned_to INTEGER REFERENCES employees(id),
                       project_id INTEGER REFERENCES projects(id),
                       due_date DATE,
                       status VARCHAR(50)
);