package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class CarInvoice implements Serializable {
    private int id;
    private Car car;
    private Slot slot;
    private ArrayList<AddedSerCom> addedSerCom;
    private LocalDateTime timeStart;
    private LocalDateTime timeEnd;
    public CarInvoice(){
        super();
    }

    public CarInvoice( Car car, Slot slot, ArrayList<AddedSerCom> addedSerCom) {
        super();
        this.car = car;
        this.slot = slot;
        this.addedSerCom = addedSerCom;
        recalcTimeRange();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Slot getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    public ArrayList<AddedSerCom> getAddedSerCom() {
        return addedSerCom;
    }

    public void setAddedSerCom(ArrayList<AddedSerCom> addedSerCom) {
        this.addedSerCom = addedSerCom;
    }

    public LocalDateTime getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(LocalDateTime timeStart) {
        this.timeStart = timeStart;
    }

    public LocalDateTime getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(LocalDateTime timeEnd) {
        this.timeEnd = timeEnd;
    }
    public void recalcTimeRange(){
        LocalDateTime minStart = null;
        LocalDateTime maxEnd = null;
        if(addedSerCom !=null){
            for(AddedSerCom asc : addedSerCom){
                if(asc.getTechSer()==null) continue;
                for(TechService ts : asc.getTechSer()){
                    if(ts ==null) continue;
                    if(minStart==null || ts.getTimeStart().isBefore(minStart)){
                        minStart = ts.getTimeStart();
                    }
                    if(maxEnd==null || ts.getTimeEnd().isAfter(maxEnd)){
                        maxEnd = ts.getTimeEnd();
                    }
                }
            }
        }
        this.timeStart = minStart;
        this.timeEnd=maxEnd;
    }
}
