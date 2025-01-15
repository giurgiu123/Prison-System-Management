package com.example.penitenciarv1.Entities;

import javafx.beans.property.SimpleStringProperty;

public class Cell {
    private SimpleStringProperty id;
    private SimpleStringProperty floor;
    private SimpleStringProperty remainingPlaces;

    public Cell() {

    }

    public Cell(String id, String floor, String remainingPlaces) {
        this.id = new SimpleStringProperty(id);
        this.floor = new SimpleStringProperty(floor);
        this.remainingPlaces = new SimpleStringProperty(remainingPlaces);
    }

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getFloor() {
        return floor.get();
    }

    public SimpleStringProperty floorProperty() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor.set(floor);
    }

    public String getRemainingPlaces() {
        return remainingPlaces.get();
    }

    public SimpleStringProperty remainingPlacesProperty() {
        return remainingPlaces;
    }

    public void setRemainingPlaces(String remainingPlaces) {
        this.remainingPlaces.set(remainingPlaces);
    }
}
