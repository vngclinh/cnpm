package model;

import java.io.Serializable;

public class ServiceComponent  implements Serializable{
    private int id;
    private String name;
    private double price;
    private String descrption;
    private int estimatedTime;
    public ServiceComponent(){
        super();
    }

    public ServiceComponent(String name, double price, String descrption, int estimatedTime) {
        super();
        this.name = name;
        this.price = price;
        this.descrption = descrption;
        this.estimatedTime = estimatedTime;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescrption() {
        return descrption;
    }

    public void setDescrption(String descrption) {
        this.descrption = descrption;
    }

    public int getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(int estimatedTime) {
        this.estimatedTime = estimatedTime;
    }
    @Override
    public String toString(){
        return this.getName();
    }
}
