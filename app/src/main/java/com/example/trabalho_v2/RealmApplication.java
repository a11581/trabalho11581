package com.example.trabalho_v2;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);

        RealmConfiguration config = new RealmConfiguration.Builder().build();


        // Start with a clean slate every time
        //Realm.deleteRealm(config);


        Realm.setDefaultConfiguration(config);

    }
}