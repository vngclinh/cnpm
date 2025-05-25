package model;

import java.io.Serializable;
import java.util.ArrayList;

public class AddedSerCom implements Serializable {
    private int id;
    private int quantity;
    private double totalAmount;
    private ServiceComponent SerCom;
    private ArrayList<TechService> TechSer;
    
    public AddedSerCom(){
        super();
    }

    public AddedSerCom(int quantity, ServiceComponent SerCom, ArrayList<TechService> TechSer) {
        super();
        this.SerCom = SerCom;
        this.quantity = quantity;
        this.totalAmount = (double)this.quantity*this.SerCom.getPrice();
        this.TechSer = TechSer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        cal();
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public ServiceComponent getSerCom() {
        return SerCom;
    }

    public void setSerCom(ServiceComponent SerCom) {
        this.SerCom = SerCom;
        cal();
    }

    private void cal(){
        if(this.SerCom!=null){
            this.totalAmount=this.quantity*this.SerCom.getPrice();
        }
    }
    public ArrayList<TechService> getTechSer() {
        return TechSer;
    }

    public void setTechSer(ArrayList<TechService> TechSer) {
        this.TechSer = TechSer;
    }
    
}
