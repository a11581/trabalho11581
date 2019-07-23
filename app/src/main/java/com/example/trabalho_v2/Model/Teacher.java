package com.example.trabalho_v2.Model;


import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.Required;

@RealmClass
public class Teacher implements RealmModel {
    @PrimaryKey
    private String id;
    @Required
    private String name;
    @Required
    private String password;
    RealmList<Discipline> Disciplines;
    RealmList<Contact> contacts;

    public RealmList<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(RealmList<Contact> contacts) {
        this.contacts = contacts;
    }

    public void setDisciplines(RealmList<Discipline> disciplines) {
        Disciplines = disciplines;
    }

    public RealmList<Discipline> getDisciplines() {
        return Disciplines;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
