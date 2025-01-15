package com.example.penitenciarv1.Entities;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Task {

    private StringProperty id;
    private StringProperty description;
    private StringProperty difficulty;
    private StringProperty startTime;
    private StringProperty endTime;
    private int markedByGuardian;

    public Task() {

    }

    public Task(String id, String description, String difficulty, String startTime, String endTime) {
        this.id = new SimpleStringProperty(id);
        this.description = new SimpleStringProperty(description);
        this.difficulty = new SimpleStringProperty(difficulty);
        this.startTime = new SimpleStringProperty(startTime);
        this.endTime = new SimpleStringProperty(endTime);
    }

    public String getId() {
        return id.get();
    }

    public StringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getDifficulty() {
        return difficulty.get();
    }

    public StringProperty difficultyProperty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty.set(difficulty);
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

    public String getEndTime() {
        return endTime.get();
    }

    public StringProperty endTimeProperty() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime.set(endTime);
    }

    public int getMarkedByGuardian() {
        return markedByGuardian;
    }

    public void setMarkedByGuardian(int markedByGuardian) {
        this.markedByGuardian = markedByGuardian;
    }
}
