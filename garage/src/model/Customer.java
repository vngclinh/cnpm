package model;
import java.io.Serializable;
public class Customer implements Serializable {
    private int id;
    private String fullname;
    private String telnum;
    private String address;
    private String note;
    
    public Customer(){
        super();
    }

    public Customer(String fullname, String telnum, String address, String note) {
        super();
        this.fullname = fullname;
        this.telnum = telnum;
        this.address = address;
        this.note = note;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
    
}
