package model;

import java.io.Serializable;


public class Car implements Serializable{
    private int id;
    private String plateNum;
    private String name;
    private String brand;
    private String type;
    public Car(){
        super();
    }

    public Car(String plateNum, String name, String brand, String type) {
        super();
        this.plateNum = plateNum;
        this.name = name;
        this.brand = brand;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlateNum() {
        return plateNum;
    }

    public void setPlateNum(String plateNum) {
        this.plateNum = plateNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
}
