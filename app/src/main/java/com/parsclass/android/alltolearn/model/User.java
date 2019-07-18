package com.parsclass.android.alltolearn.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import android.util.Patterns;

import com.parsclass.android.alltolearn.Utils.DateConverter;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "user_table")
@TypeConverters({DateConverter.class})
public class User implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private String email;
    private String token;
    private String imagePath;
    @Ignore
    private String password;
    private Date lastRefresh;

    public User(String name, String email, String token) {

        this.name = name;
        this.email = email;
        this.token = token;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }



    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getLastRefresh() {
        return lastRefresh;
    }

    public void setLastRefresh(Date lastRefresh) {
        this.lastRefresh = lastRefresh;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isEmailValid() {
        return Patterns.EMAIL_ADDRESS.matcher(getEmail()).matches();
    }


    public boolean isPasswordLengthGreaterThanMin() {
        return getPassword().length() > 6;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
