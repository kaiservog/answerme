package com.answerme.http.access;

/**
 * Created by kaiservog on 25/02/2016.
 */
public enum AnswermeUrl {
    CHECK("/user/check"), QUESTION_ADD("/question/add"), USER_UPDATE("/user/update"),
    QUESTION_FIND("/question/find");

    private String url;
    private AnswermeUrl(String url) {this.url = url;}

    public String toString() {
        return url;
    }
}
