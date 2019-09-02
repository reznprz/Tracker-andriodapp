package com.example.macbookpro.tracker;

public class Customer {

    String firstName, lastname, password, phone, email;

    public Customer(String firstName, String lastName, String password, String phone, String email) {
        firstName = firstName;
        lastName = lastName;
        password = password;
        phone = phone;
        email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {

        return getFirstName()+getLastname()+"@smsu.edu";

    }

    public void setEmail(String email) {
        this.email = email;
    }


}

