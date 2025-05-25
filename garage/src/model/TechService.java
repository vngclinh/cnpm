package model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class TechService implements Serializable {
    private int id;
    private LocalDateTime timeStart;
    private LocalDateTime timeEnd;
    private Technician tech;
    
    public TechService(){
        super();
    }

    public TechService(LocalDateTime timeStart, LocalDateTime timeEnd, Technician techniciandID) {
        super();
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.tech = techniciandID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Technician getTech() {
        return tech;
    }

    public void setTech(Technician tech) {
        this.tech = tech;
    }
    
    
}
