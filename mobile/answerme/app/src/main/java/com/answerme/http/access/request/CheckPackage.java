package com.answerme.http.access.request;

/**
 * Created by kaiservog on 25/02/2016.
 */
public class CheckPackage extends RequestPackage {
    private String name;
    private String extUserId;

    public CheckPackage(String token, String loginService, String name, String extUserId) {
        super(token, loginService);
        this.name = name;
        this.extUserId = extUserId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtUserId() {
        return extUserId;
    }

    public void setExtUserId(String extUserId) {
        this.extUserId = extUserId;
    }
}
