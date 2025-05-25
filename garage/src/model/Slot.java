package model;

import java.io.Serializable;

public class Slot implements Serializable {
    private int id;
    private String name;
    public Slot(){
        super();
    }
    public Slot(String name){
        super();
        this.name=name;
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
    
}
