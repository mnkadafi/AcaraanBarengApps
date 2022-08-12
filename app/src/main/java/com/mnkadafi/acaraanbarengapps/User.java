package com.mnkadafi.acaraanbarengapps;

public class User {
    public String idUser;
    public String fullName;
    public String gender;
    public String location;
    public String phone;
    public String email;
    public String password;

    public User() {

    }

    public User(String idUser, String fullName, String gender, String location, String phone, String email, String password) {
        this.idUser = idUser;
        this.fullName = fullName;
        this.gender = gender;
        this.location = location;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }

    public String getIdUser() { return idUser; }

    public String getFullName() {
        return fullName;
    }

    public String getGender() {
        return gender;
    }

    public String getLocation() {
        return location;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }
}
