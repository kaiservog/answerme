package com.answerme.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by kaiservog on 25/02/2016.
 */
public class BackendAccessService extends IntentService {

    public BackendAccessService() {
        super("BackendAccessService");
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        while(true) {
            Log.i("Backend", "teste");
            try {
                Thread.sleep(1000L);
            }catch (Exception e) {}
        }
    }

}
