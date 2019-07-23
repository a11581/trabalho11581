package com.example.trabalho_v2.Model;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.Required;

@RealmClass
public class Contact implements RealmModel{
    @PrimaryKey
    private String id;
    @Required
    private Integer startingHour;
    @Required
    private Integer endingHour;
    @Required
    private String type;
    @Required
    private String day;

    public void setId(String id) {
        this.id = id;
    }

    public void setStartingHour(Integer startingHour) {
        this.startingHour = startingHour;
    }

    public void setEndingHour(Integer endingHour) {
        this.endingHour = endingHour;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getId() {
        return id;
    }

    public Integer getStartingHour() {
        return startingHour;
    }

    public Integer getEndingHour() {
        return endingHour;
    }

    public String getType() {
        return type;
    }

    public String getDay() {
        return day;
    }
}
