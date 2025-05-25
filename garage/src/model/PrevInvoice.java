package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class PrevInvoice implements Serializable{
    private int id;
    private LocalDateTime createdAt;
    private User user;
    private Customer customer;
    private double total;
    private ArrayList<CarInvoice> carBill;
    public PrevInvoice(){
        super();
    }

    public PrevInvoice(LocalDateTime createdAt, User user, Customer customer, ArrayList<CarInvoice> carBill) {
        this.createdAt = createdAt;
        this.user = user;
        this.customer = customer;
        this.carBill = carBill;
        this.total = calculateTotal();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUserID(User userID) {
        this.user = userID;
    }

    public Customer getCustomerID() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public double getTotal() {
        return calculateTotal();
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public ArrayList<CarInvoice> getCarBill() {
        return carBill;
    }

    public void setCarBill(ArrayList<CarInvoice> carBill) {
        this.carBill = carBill;
    }
    private double calculateTotal() {
        double sum = 0;
        if (carBill != null) {
            for (CarInvoice ci : carBill) {
                if (ci.getAddedSerCom() != null) {
                    for (AddedSerCom asc : ci.getAddedSerCom()) {
                        sum += asc.getTotalAmount();
                    }
                }
            }
        }
        System.out.println("sum"+sum);
        return sum;
    }

}
