package com.CobaltConnect1.application;

import android.app.Application;
import android.os.Environment;

import java.io.File;

/**
 * Created by abhinandan on 14/11/16.
 */

public class AppController extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //ActiveAndroid.initialize(this);
      //  createDirectory();

    }

    private void createDirectory() {
        new File(Environment.getExternalStorageDirectory(), "DocChat/SENT/IMG").mkdirs();
        new File(Environment.getExternalStorageDirectory(), "DocChat/RECEIVED/IMG").mkdirs();
        new File(Environment.getExternalStorageDirectory(), "DocChat/SENT/FILE").mkdirs();
        new File(Environment.getExternalStorageDirectory(), "DocChat/RECEIVED/FILE").mkdirs();

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
      // StandardMethods.startService(this);
    }
}

