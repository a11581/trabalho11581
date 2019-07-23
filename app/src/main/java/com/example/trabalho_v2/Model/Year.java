package com.example.trabalho_v2.Model;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.Required;

@RealmClass
public class Year implements RealmModel {
    @PrimaryKey
    private String id;
    @Required
    private Integer yearDate;
    @Required
    private String username;


    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getId() {
        return id;
    }

    public Integer getYearDate() {
        return yearDate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setYearDate(Integer yearDate) {
        this.yearDate = yearDate;
    }
}
