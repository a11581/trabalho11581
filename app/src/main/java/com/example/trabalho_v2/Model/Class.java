package com.example.trabalho_v2.Model;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.Required;

@RealmClass
public class Class implements RealmModel {
    @PrimaryKey
    private String id;
    @Required
    private Integer startingHour;
    @Required
    private Integer duration;
    @Required
    private String date;
    @Required
    private String room;
    @Required
    private Boolean special;
    @Required
    private String disciplineName;
    RealmList<Topic> topics;

    public void setTopics(RealmList<Topic> topics) {
        this.topics = topics;
    }

    public RealmList<Topic> getTopics() {
        return topics;
    }

    public void setDisciplineName(String disciplineName) {
        this.disciplineName = disciplineName;
    }

    public String getDisciplineName() {
        return disciplineName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStartingHour(Integer startingHour) {
        this.startingHour = startingHour;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setSpecial(Boolean special) {
        this.special = special;
    }

    public String getId() {
        return id;
    }

    public Integer getStartingHour() {
        return startingHour;
    }

    public Integer getDuration() {
        return duration;
    }

    public String getDate() {
        return date;
    }

    public String getRoom() {
        return room;
    }

    public Boolean getSpecial() {
        return special;
    }
}
