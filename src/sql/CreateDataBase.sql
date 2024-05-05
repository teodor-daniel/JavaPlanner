DROP TABLE IF EXISTS timelogs;
DROP TABLE IF EXISTS tasks;
DROP TABLE IF EXISTS projects;
DROP TABLE IF EXISTS employees;
DROP TABLE IF EXISTS departments;
DROP TABLE IF EXISTS company;

CREATE TABLE company (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         address VARCHAR(255),
                         registration_number VARCHAR(100),
                         phone_number VARCHAR(50),
                         email VARCHAR(100)
);

CREATE TABLE departments (
                             id SERIAL PRIMARY KEY,
                             name VARCHAR(255) NOT NULL,
                             company_id INTEGER NOT NULL,
                             manager_id INTEGER,
                             FOREIGN KEY (manager_id) REFERENCES employees(id) ON DELETE CASCADE,
                             FOREIGN KEY (company_id) REFERENCES company(id) ON DELETE CASCADE
);


CREATE TABLE employees (
                           id SERIAL PRIMARY KEY,
                           name VARCHAR(255) NOT NULL,
                           age INT,
                           phone_number VARCHAR(20) UNIQUE,
                           email VARCHAR(255) UNIQUE,
                           salary DOUBLE PRECISION,
                           department_id INTEGER NOT NULL,
                           employed_status VARCHAR(255) NOT NULL,
                           bonus INT DEFAULT NULL,
                           team_lead_id INTEGER DEFAULT NULL,
                           company_id INTEGER NOT NULL,
                           FOREIGN KEY (team_lead_id) REFERENCES employees(id) ON DELETE SET NULL,
                           FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE CASCADE,
                           FOREIGN KEY (company_id) REFERENCES company(id) ON DELETE CASCADE
);

CREATE TABLE projects (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          lead_id INTEGER NOT NULL,
                          status VARCHAR(50) NOT NULL DEFAULT 'Not started', -- Reflects the abstract status
                          department_id INTEGER NOT NULL,
                          budget DOUBLE PRECISION,
                          FOREIGN KEY (lead_id) REFERENCES employees(id),
                          FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE CASCADE
);


CREATE TABLE tasks (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       description TEXT,
                       assigned_to INTEGER,
                       project_id INTEGER,
                       due_date DATE,
                       status VARCHAR(50) NOT NULL,
                       FOREIGN KEY (assigned_to) REFERENCES employees(id) ON DELETE SET NULL,
                       FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
);

CREATE TABLE timelogs (
                          id SERIAL PRIMARY KEY,
                          task_id INTEGER NOT NULL,
                          employee_id INTEGER NOT NULL,
                          hours_logged DOUBLE PRECISION,
                          log_date DATE NOT NULL,
                          description TEXT,
                          FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE,
                          FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE
);
