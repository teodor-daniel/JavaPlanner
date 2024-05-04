package org.example.Validation;

import org.example.Interfaces.ValidationService;
import org.example.Models.TimeLog;

public class TimeLogValidationService implements ValidationService<TimeLog> {

    @Override
    public boolean validate(TimeLog timeLog) {
        return timeLog != null &&
                timeLog.getTaskId() > 0 &&
                timeLog.getEmployeeId() > 0 &&
                timeLog.getHoursLogged() > 0 &&
                timeLog.getLogDate() != null;
    }
}
