package com.sxq.backgammon;

import android.app.Application;

import com.sxq.backgammon.util.ServicesLog;

/**
 * Created by SXQ on 2017/6/20.
 */

public class App extends Application {

    private static App sInstance ;

    public static App getInstance(){
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        ServicesLog.enable(true);
    }
}
