package com.theironyard.entities;

import com.theironyard.utilities.PasswordStorage;

import javax.persistence.*;

/**
 * Created by Zach on 10/6/16.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    int id;

    @Column(nullable = false, unique = true)
    String name;

    @Column(nullable = false)
    String password;

    public User() {
    }

    public User(String name, String password) throws PasswordStorage.CannotPerformOperationException {
        this.name = name;
        this.password = PasswordStorage.createHash(password);
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

    private String getPassword() {
        return password;
    }

    public void setPassword(String password) throws PasswordStorage.CannotPerformOperationException {
        this.password = PasswordStorage.createHash(password);
    }
    public boolean isValid(String password) throws Exception{
        if(PasswordStorage.verifyPassword(password,getPassword())){
            return true;
        }
        else{
            return false;
        }
    }
}
