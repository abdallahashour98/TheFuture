package com.example.TheFuture.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;
import java.io.Serializable;


public class ModelData implements Serializable {
    @Exclude
    private String ID;
    public String teacherName;
    public String subjectName;
    public String studyYear;
    public Timestamp Time;
    public String Room;
    public String Day;
    public String dayOF;
    public String onlyDayOfWeek;


    public ModelData(String ID, String teacherName, String subjectName, String studyYear, Timestamp time, String room, String day, String dayOF, String onlyDayOfWeek) {
        this.ID = ID;
        this.teacherName = teacherName;
        this.subjectName = subjectName;
        this.studyYear = studyYear;
        Time = time;
        Room = room;
        Day = day;
        this.dayOF = dayOF;
        this.onlyDayOfWeek = onlyDayOfWeek;
    }

    public ModelData(String ID, String teacherName, String subjectName, String studyYear, Timestamp time, String room, String day, String dayOF) {
        this.ID = ID;
        this.teacherName = teacherName;
        this.subjectName = subjectName;
        this.studyYear = studyYear;
        Time = time;
        Room = room;
        Day = day;
        this.dayOF = dayOF;
    }

    public ModelData() {
    }

    public String getOnlyDayOfWeek() {
        return onlyDayOfWeek;
    }

    public void setOnlyDayOfWeek(String onlyDayOfWeek) {
        this.onlyDayOfWeek = onlyDayOfWeek;
    }

    public String getDayOF() {
        return dayOF;
    }

    public void setDayOF(String dayOF) {
        this.dayOF = dayOF;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }
    public String getRoom() {
        return Room;
    }

    public void setRoom(String room) {
        Room = room;
    }
    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getStudyYear() {
        return studyYear;
    }

    public void setStudyYear(String studyYear) {
        this.studyYear = studyYear;
    }

    public Timestamp getTime() {
        return Time;
    }

    public void setTime(Timestamp time) {
        Time = time;
    }
}
