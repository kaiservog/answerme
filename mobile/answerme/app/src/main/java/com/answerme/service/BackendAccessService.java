package com.answerme.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.answerme.http.access.AnswermeUrl;
import com.answerme.http.access.BackendSender;
import com.answerme.http.access.request.QuestionFindPackage;

/**
 * Created by kaiservog on 25/02/2016.
 */
public class BackendAccessService extends IntentService {

    public BackendAccessService() {
        super("BackendAccessService");
    }
    private String token;
    private String loginService;

    private boolean finding = true;

    public BackendAccessService(String name, String token, String loginService) {
        super(name);
        this.token = token;
        this.loginService = loginService;
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        while(finding) {
            String resp = BackendSender.sendPost(AnswermeUrl.QUESTION_FIND,
                    new QuestionFindPackage(token, loginService));

            if(resp != null) Log.i("backend", resp);
            try {
                Thread.sleep(1000L);
            }catch (Exception e) {}
        }
    }

    public void stop() {
        this.finding = false;
    }

    public void start() {
        this.finding = true;
    }

}
