package com.example.TheFuture.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class ModelNotes implements Serializable {

    private Timestamp Time;
    private String Notes;
    @Exclude
    private String ID;

    public ModelNotes() {
    }

    public ModelNotes(Timestamp time, String notes, String ID) {
        Time = time;
        Notes = notes;
        this.ID = ID;
    }

    public Timestamp getTime() {
        return Time;
    }

    public void setTime(Timestamp time) {
        Time = time;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }
}
