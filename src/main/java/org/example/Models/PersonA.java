package org.example.Models;

import java.util.Objects;

public abstract class PersonA {
    private int age;

    private String name;

    private String phoneNumber;

    private String email;


    public PersonA(String name, int age, String phoneNumber, String email) {
        this.age = age;
        this.name = name;
        setPhoneNumber(phoneNumber);
        setEmail(email);
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {//to do ragex validation for phone and email.
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
        PersonA person = (PersonA) o;
        return age == person.age && Objects.equals(name, person.name) && Objects.equals(phoneNumber, person.phoneNumber) && Objects.equals(email, person.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(age, name, phoneNumber, email);
    }

    @Override
    public String toString() {
        return
                ", age=" + age +
                        ", name='" + name + '\'' +
                        ", phoneNumber='" + phoneNumber + '\'' +
                        ", email='" + email + '\''
                ;
    }
}
