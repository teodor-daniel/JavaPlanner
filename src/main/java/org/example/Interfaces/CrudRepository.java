package org.example.Interfaces;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, ID> {
    void save(Connection conn, T entity);
    void update(Connection conn, T entity);
    void deleteById(Connection conn, ID id);
    Optional<T> findById(Connection conn, ID id);
    List<T> findAll(Connection conn);
}
