package com.answerme.http.access.request;

import java.util.List;

/**
 * Created by kaiservog on 25/02/2016.
 */
public class UserUpdatePackage extends RequestPackage {
    private List<String> topics;

    public UserUpdatePackage(String token, String loginService, List<String> topics) {
        super(token, loginService);
        this.topics = topics;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }
}
