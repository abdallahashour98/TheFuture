package com.example.TheFuture.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class ModelDiary implements Serializable {
    @Exclude
    private String ID;
    String name ;
    Long totalDiary ;
    Long existingDiary ;
    Long remainingDiary ;

    String check;
    public Timestamp Time;


    public ModelDiary() {

    }

    public ModelDiary(String ID, String name, String check) {
        this.ID = ID;
        this.name = name;
        this.check = check;
    }

    public ModelDiary(String ID, String name, String check, Timestamp time) {
        this.ID = ID;
        this.name = name;
        this.check = check;
        Time = time;
    }

    public ModelDiary(String ID, Long totalDiary, Long existingDiary, Long remainingDiary) {
        this.ID = ID;
        this.totalDiary = totalDiary;
        this.existingDiary = existingDiary;
        this.remainingDiary = remainingDiary;
    }

    public ModelDiary(Long totalDiary, Long existingDiary, Long remainingDiary) {
        this.totalDiary = totalDiary;
        this.existingDiary = existingDiary;
        this.remainingDiary = remainingDiary;
    }

    public Long getTotalDiary() {
        return totalDiary;
    }

    public void setTotalDiary(Long totalDiary) {
        this.totalDiary = totalDiary;
    }

    public Long getExistingDiary() {
        return existingDiary;
    }

    public void setExistingDiary(Long existingDiary) {
        this.existingDiary = existingDiary;
    }

    public Long getRemainingDiary() {
        return remainingDiary;
    }

    public void setRemainingDiary(Long remainingDiary) {
        this.remainingDiary = remainingDiary;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public Timestamp getTime() {
        return Time;
    }

    public void setTime(Timestamp time) {
        Time = time;
    }
}
