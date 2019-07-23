package com.example.trabalho_v2.Model;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.Required;

@RealmClass
public class Discipline implements RealmModel {
    @PrimaryKey
    private String id;
    @Required
    private String name;
    @Required
    private String acronym;
    @Required
    private String period;
    private Year year;
    private Course course;
    RealmList<Class> classes;
    RealmList<Date> dates;

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getPeriod() {
        return period;
    }

    public RealmList<Date> getDates() {
        return dates;
    }

    public void setDates(RealmList<Date> dates) {
        this.dates = dates;
    }

    public void setClasses(RealmList<Class> classes) {
        this.classes = classes;
    }

    public RealmList<Class> getClasses() {
        return classes;
    }

    public void setYear(Year year) {
        this.year = year;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Year getYear() {
        return year;
    }

    public Course getCourse() {
        return course;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAcronym() {
        return acronym;
    }
}
