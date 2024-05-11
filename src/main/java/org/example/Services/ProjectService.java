package org.example.Services;

import org.example.Crud.CRUDprojects;
import org.example.Validation.ProjectValidation;
import org.example.Models.Project;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class ProjectService {
    private final CRUDprojects crudProjects;
    private final ProjectValidation projectValidator;

    public ProjectService(CRUDprojects crudProjects, ProjectValidation projectValidator) {
        this.crudProjects = crudProjects;
        this.projectValidator = projectValidator;
    }

    public void addProject(Connection conn, Project project) {
        if (!projectValidator.validate(project)) {
            throw new IllegalArgumentException("Invalid project data");
        }

        crudProjects.save(conn, project);
    }

    public List<Project> getAllProjects(Connection conn) {
        return crudProjects.findAll(conn);
    }

    public Optional<Project> getProjectById(Connection conn, Integer id) {
        return crudProjects.findById(conn, id);
    }

    public void updateProject(Connection conn, Project project) {
        if (!projectValidator.validate(project)) {
            throw new IllegalArgumentException("Invalid project data");
        }

        crudProjects.update(conn, project);
    }

    public void deleteProjectById(Connection conn, Integer id) {
        crudProjects.deleteById(conn, id);
    }
}
