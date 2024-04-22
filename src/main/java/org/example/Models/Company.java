package org.example.Models;

import java.util.Objects;

public class Company {
    private int id;

    private String name;

    private String address;

    private String registrationNumber;

    private String phoneNumber;

    private String email;

    public Company(int id, String name, String address, String registrationNumber, String phoneNumber, String email) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.registrationNumber = registrationNumber;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public Company(String name, String address, String registrationNumber, String phoneNumber, String email) {
        this.name = name;
        this.address = address;
        this.registrationNumber = registrationNumber;
        this.phoneNumber = phoneNumber;
        this.email = email;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return id == company.id && Objects.equals(name, company.name) && Objects.equals(address, company.address) && Objects.equals(registrationNumber, company.registrationNumber) && Objects.equals(phoneNumber, company.phoneNumber) && Objects.equals(email, company.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, registrationNumber, phoneNumber, email);
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", registrationNumber='" + registrationNumber + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
