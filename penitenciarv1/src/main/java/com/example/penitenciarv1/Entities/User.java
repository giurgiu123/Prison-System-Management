package com.example.penitenciarv1.Entities;

public class User {
    // 0 for the prison chief
    // 1 for the warden
    // 2 for the inmate
    // 3 for the visitor
    private int id;
    private int accessRights = 2;
    public User(int id, int accessRights) {
        this.id = id;
        this.accessRights = accessRights;
    }
    public User(){

    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getAccessRights() {
        return accessRights;
    }
    public void setAccessRights(int accessRights) {
        this.accessRights = accessRights;
    }

    public void printUser() {
        System.out.println("ID: " + id + " Access Rights: " + accessRights);
    }
}
