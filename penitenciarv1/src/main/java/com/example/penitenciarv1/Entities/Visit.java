package com.example.penitenciarv1.Entities;

import eu.hansolo.toolbox.time.DateTimes;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Visit {
    private SimpleStringProperty  idVisit;
    private SimpleStringProperty  visitType;
    private SimpleStringProperty  startTime;
    private SimpleStringProperty  endTime;
    private SimpleStringProperty idInmate;
    private SimpleStringProperty  inmateName;
    private SimpleStringProperty date;
    private SimpleStringProperty hourTime;
    private SimpleStringProperty visitorName;
    public Visit() {
        this.idVisit = new SimpleStringProperty("0");
        this.visitType = new SimpleStringProperty("0");
        this.startTime = new SimpleStringProperty("0");
        this.endTime = new SimpleStringProperty("0");
        this.idInmate = new SimpleStringProperty("0");
        this.inmateName = new SimpleStringProperty("0");
        this.visitorName = new SimpleStringProperty("0");
    }
    public Visit(String idVisit, String visitType, String startTime, String endTime, String idInmate) {
        this.idVisit = new SimpleStringProperty(idVisit);
        this.visitType = new SimpleStringProperty(visitType);
        this.startTime = new SimpleStringProperty(startTime);
        this.endTime = new SimpleStringProperty(endTime);
        this.idInmate = new SimpleStringProperty(idInmate);

    }

    public Visit(String startTime, String endTime, String nume) {
        this.idVisit = new SimpleStringProperty("0");
        this.visitType = new SimpleStringProperty("1");
        this.startTime = new SimpleStringProperty(startTime);
        this.endTime = new SimpleStringProperty(endTime);
        this.idInmate = new SimpleStringProperty("0");
        this.inmateName = new SimpleStringProperty(nume);

    }

    public StringProperty getIdVisit() {
        return idVisit;
    }

    public void setIdVisit(String idVisit) {
        this.idVisit.set(idVisit);
    }

    public SimpleStringProperty getVisitType() {
        return visitType;
    }

    public void setVisitType(String visitType) {
        this.visitType.set(visitType);
    }

    public SimpleStringProperty getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime.set(startTime) ;
    }

    public SimpleStringProperty getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime.set(endTime);
    }

    public SimpleStringProperty getIdInmate() {
        return idInmate;
    }

    public void setIdInmate(String idInmate) {
        this.idInmate.set(idInmate);
    }

    public StringProperty getInmateName() {
        return inmateName;
    }

    public SimpleStringProperty inmateNameProperty() {
        return inmateName;
    }

    public void setInmateName(String inmateName) {
        this.inmateName.set(inmateName);
    }

    public void setDate(String date) {
        String[] strings = date.split(" ");
        this.date = new SimpleStringProperty(strings[0]);
        this.hourTime = new SimpleStringProperty(strings[1]);

    }

    public String getVisitorName() {
        return visitorName.get();
    }

    public SimpleStringProperty visitorNameProperty() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName.set(visitorName);
    }
}
