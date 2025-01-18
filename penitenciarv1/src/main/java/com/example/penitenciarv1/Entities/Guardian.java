package com.example.penitenciarv1.Entities;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Guardian {
    private StringProperty id;
    private StringProperty username;
    private StringProperty floor;
    private StringProperty detentionBlock;

    public Guardian() {

    }

    public Guardian(String id, String username, String floor, String detentionBlock) {
        this.id = new SimpleStringProperty(id);
        this.username = new SimpleStringProperty(username);
        this.floor = new SimpleStringProperty(floor);
        this.detentionBlock = new SimpleStringProperty(detentionBlock);
    }

    public StringProperty getId() {
        return id;
    }

    public void setId(StringProperty id) {
        this.id = id;
    }

    public StringProperty getUsername() {
        return username;
    }

    public void setUsername(StringProperty username) {
        this.username = username;
    }

    public StringProperty getFloor() {
        return floor;
    }

    public void setFloor(StringProperty floor) {
        this.floor = floor;
    }

    public StringProperty getDetentionBlock() {
        return detentionBlock;
    }

    public void setDetentionBlock(StringProperty detentionBlock) {
        this.detentionBlock = detentionBlock;
    }
}
