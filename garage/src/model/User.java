package model;

import java.io.Serializable;

public class User implements Serializable{
    private int id;
    private String username;
    private String password;
    private String fullname;
    private String role;

    public User() {
            super();
    }

    public User(String username, String password, String name, String position) {
            super();
            this.username = username;
            this.password = password;
            this.fullname = name;
            this.role = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
        
	
}
