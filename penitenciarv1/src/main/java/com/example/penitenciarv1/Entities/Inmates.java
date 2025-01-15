package com.example.penitenciarv1.Entities;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Inmates {
    private StringProperty name;
    private StringProperty id;
    private StringProperty sentenceRemained;
    private StringProperty profession;
    private StringProperty idCelula;

    public Inmates(String name, String id, String sentenceRemained) {

        this.name = new SimpleStringProperty(name);
        this.id = new SimpleStringProperty(id);
        this.sentenceRemained = new SimpleStringProperty(sentenceRemained);
        this.profession = new SimpleStringProperty("");
    }

    public Inmates() {
        this.name = new SimpleStringProperty("");
        this.id = new SimpleStringProperty("");
        this.sentenceRemained = new SimpleStringProperty("");
        this.profession = new SimpleStringProperty("");
    }

    public StringProperty getid() {
        return id;
    }

    public void setid(StringProperty id) {
        this.id = id;
    }

    public StringProperty getName() {
        return name;
    }

    public void setName(StringProperty name) {
        this.name = name;
    }

    public void setName(String name){
        this.name = new SimpleStringProperty(name);
    }

    public StringProperty getSentenceRemained() {
        return sentenceRemained;
    }

    public void setSentenceRemained(StringProperty sentenceRemained) {
        this.sentenceRemained = sentenceRemained;
    }

    public StringProperty getProfession() {
        return profession;
    }

    public void setProfession(StringProperty profession) {
        this.profession = profession;
    }

    public void setIdCelula(StringProperty idCelula) {
        this.idCelula = idCelula;
    }

    public StringProperty getIdCelula() {
        return idCelula;
    }


}
