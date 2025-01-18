package com.example.penitenciarv1.Entities;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Sentence {

    private StringProperty category;
    private StringProperty specificReason;
    private StringProperty startTime;
    private StringProperty endTime;
    public Sentence(String category, String specificReason, String startTime, String endTime) {

        this.category = new SimpleStringProperty(category);
        this.specificReason = new SimpleStringProperty(specificReason);
        this.startTime = new SimpleStringProperty(startTime);
        this.endTime = new SimpleStringProperty(endTime);
    }
    public Sentence() {
        category = new SimpleStringProperty("");
        specificReason = new SimpleStringProperty("");
        startTime = new SimpleStringProperty("");
        endTime = new SimpleStringProperty("");
    }

    public String getEndTime() {
        return endTime.get();
    }

    public StringProperty endTimeProperty() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime.set(endTime);
    }

    public String getStartTime() {
        return startTime.get();
    }

    public StringProperty startTimeProperty() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime.set(startTime);
    }

    public String getspecificReason() {
        return specificReason.get();
    }

    public StringProperty specificReasonProperty() {
        return specificReason;
    }

    public void setspecificReason(String specificReason) {
        this.specificReason.set(specificReason);
    }

    public String getcategory() {
        return category.get();
    }

    public StringProperty categoryProperty() {
        return category;
    }

    public void setcategory(String category) {
        this.category.set(category);
    }


}
