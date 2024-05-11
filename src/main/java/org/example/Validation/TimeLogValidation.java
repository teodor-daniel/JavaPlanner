package org.example.Validation;

import org.example.Interfaces.IValidation;
import org.example.Models.TimeLog;
import org.example.Exceptions.*;

public class TimeLogValidation implements IValidation<TimeLog> {

    @Override
    public boolean validate(TimeLog timeLog) {
        if (timeLog == null) {
            throw new InvalidTimeLogException("TimeLog object cannot be null.");
        }
        validateTaskId(timeLog.getTaskId());
        validateEmployeeId(timeLog.getEmployeeId());
        validateHoursLogged(timeLog.getHoursLogged());
        return true;
    }

    private void validateTaskId(int taskId) {
        if (taskId <= 0) {
            throw new InvalidTaskIdException("Error: Task ID is invalid. It must be a positive integer.");
        }
    }

    private void validateEmployeeId(int employeeId) {
        if (employeeId <= 0) {
            throw new InvalidEmployeeIdException("Error: Employee ID is invalid. It must be a positive integer.");
        }
    }

    private void validateHoursLogged(int hoursLogged) {
        if (hoursLogged <= 0) {
            throw new InvalidHoursLoggedException("Error: Hours logged must be greater than zero.");
        }
    }

}
