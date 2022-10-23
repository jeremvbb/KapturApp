package com.example.myapplication;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class User {
    String name;
    String lastname;
    String email;
    String password;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {

        return password;
    }
    public static String getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[32];
        sr.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);

    }
    public void setPassword(String password) {

        this.password = password;

    }



    public User(String name, String lastname, String email, String password){
        this.name=name;
        this.lastname=lastname;
        this.email=email;
        this.password=password;

    };
}
