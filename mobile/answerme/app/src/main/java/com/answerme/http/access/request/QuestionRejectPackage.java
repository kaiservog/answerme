package com.answerme.http.access.request;

/**
 * Created by kaiservog on 25/02/2016.
 */
public class QuestionRejectPackage extends RequestPackage {
    private String questionId;

    public QuestionRejectPackage(String token, String loginService, String questionId) {
        super(token, loginService);
        this.questionId = questionId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }
}
