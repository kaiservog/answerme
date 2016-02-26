package com.answerme.http.access.request;

import com.google.gson.Gson;

/**
 * Created by kaiservog on 25/02/2016.
 */
public class RequestPackage {
    private String token;
    private String loginService;

    public RequestPackage(String token, String loginService) {
        this.token = token;
        this.loginService = loginService;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLoginService() {
        return loginService;
    }

    public void setLoginService(String loginService) {
        this.loginService = loginService;
    }

    public String toJson(){
        return new Gson().toJson(this);
    }
}
