package com.fefeyo.otasuke;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by fefe on 2017/02/20.
 */

public class MainApplication  extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder().build());
    }
}
