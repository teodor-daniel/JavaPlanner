    package org.example.Models;

    import java.sql.Date;

    public class TimeLog {
        private Integer id;
        private Integer taskId;
        private Integer employeeId;
        private Double hoursLogged;
        private Date logDate;
        private String description;

        public TimeLog(Integer id, Integer taskId, Integer employeeId, Double hoursLogged, Date logDate, String description) {
            this.id = id;
            this.taskId = taskId;
            this.employeeId = employeeId;
            this.hoursLogged = hoursLogged;
            this.logDate = logDate;
            this.description = description;
        }

        public TimeLog(Integer taskId, Integer employeeId, Double hoursLogged, Date logDate, String description) {
            this.id = id;
            this.taskId = taskId;
            this.employeeId = employeeId;
            this.hoursLogged = hoursLogged;
            this.logDate = logDate;
            this.description = description;
        }


        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getTaskId() {
            return taskId;
        }

        public void setTaskId(Integer taskId) {
            this.taskId = taskId;
        }

        public Integer getEmployeeId() {
            return employeeId;
        }

        public void setEmployeeId(Integer employeeId) {
            this.employeeId = employeeId;
        }

        public Double getHoursLogged() {
            return hoursLogged;
        }

        public void setHoursLogged(Double hoursLogged) {
            this.hoursLogged = hoursLogged;
        }

        public Date getLogDate() {
            return logDate;
        }

        public void setLogDate(Date logDate) {
            this.logDate = logDate;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return "TimeLog{" +
                    "id=" + id +
                    ", taskId=" + taskId +
                    ", employeeId=" + employeeId +
                    ", hoursLogged=" + hoursLogged +
                    ", logDate=" + logDate +
                    ", description='" + description + '\'' +
                    '}';
        }
    }
