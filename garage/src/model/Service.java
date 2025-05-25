package model;

public class Service extends ServiceComponent{
    public Service(){
        super();
    }
    public Service(String name, double price, String description, int estimatedTime){
        super(name, price, description, estimatedTime);
    }
}
