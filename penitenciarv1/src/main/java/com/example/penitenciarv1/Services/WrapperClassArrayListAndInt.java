package com.example.penitenciarv1.Services;

import com.example.penitenciarv1.Entities.Visit;

import java.util.ArrayList;

public class WrapperClassArrayListAndInt {
    private ArrayList<Visit> visitList;
    private int [] visitId;

    public WrapperClassArrayListAndInt() {

    }

    public WrapperClassArrayListAndInt(ArrayList<Visit> visitList, int [] visitId) {
        this.visitList = visitList;
        this.visitId = visitId;
    }

    public ArrayList<Visit> getVisitList() {
        return visitList;
    }

    public int [] getVisitId() {
        return visitId;
    }
}
