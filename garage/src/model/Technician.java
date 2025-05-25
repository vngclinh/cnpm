package model;

import java.io.Serializable;


public class Technician implements Serializable {
    private int id;
    private String fullname;
    private String telnum;
    private String address;
    
    public Technician(){
        super();
    }

    public Technician(String fullname, String telnum, String address) {
        super();
        this.fullname = fullname;
        this.telnum = telnum;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getTelnum() {
        return telnum;
    }

    public void setTelnum(String telnum) {
        this.telnum = telnum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
}
