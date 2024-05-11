package org.example.Services;

import org.example.Crud.CRUDtimeLogs;
import org.example.Validation.TimeLogValidation;
import org.example.Models.TimeLog;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class TimeLogService {
    private final CRUDtimeLogs crudTimeLogs;
    private final TimeLogValidation timeLogValidator;

    public TimeLogService(CRUDtimeLogs crudTimeLogs, TimeLogValidation timeLogValidator) {
        this.crudTimeLogs = crudTimeLogs;
        this.timeLogValidator = timeLogValidator;
    }

    public void addTimeLog(Connection conn, TimeLog timeLog) {
        if (!timeLogValidator.validate(timeLog)) {
            throw new IllegalArgumentException("Invalid time log data");
        }

        crudTimeLogs.save(conn, timeLog);
    }

    public List<TimeLog> getAllTimeLogs(Connection conn) {
        return crudTimeLogs.findAll(conn);
    }

    public Optional<TimeLog> getTimeLogById(Connection conn, Integer id) {
        return crudTimeLogs.findById(conn, id);
    }

    public void updateTimeLog(Connection conn, TimeLog timeLog) {
        if (!timeLogValidator.validate(timeLog)) {
            throw new IllegalArgumentException("Invalid time log data");
        }

        crudTimeLogs.update(conn, timeLog);
    }

    public void deleteTimeLogById(Connection conn, Integer id) {
        crudTimeLogs.deleteById(conn, id);
    }
}
