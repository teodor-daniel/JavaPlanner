package org.example.Models;


import java.util.Objects;

public class Department {
    private int id;
    private String name;
    private int companyId;
    private Integer managerId;

    public Department(int id, String name, int companyId, Integer managerId) {
        this.id = id;
        this.name = name;
        this.companyId = companyId;
        this.managerId = managerId;
    }

    public Department(String name, int companyId) {
        this.name = name;
        this.companyId = companyId;
        this.managerId = null;
    }



    public Department(int departmentId, Object o, int i) {
        this.id = departmentId;
        this.name = "";
        this.companyId = 0;
        this.managerId = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return id == that.id && companyId == that.companyId && Objects.equals(name, that.name) && Objects.equals(managerId, that.managerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, companyId, managerId);
    }

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", companyId=" + companyId +
                ", managerId=" + managerId +
                '}';
    }
}
