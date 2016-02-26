package com.answerme.http.access.request;

/**
 * Created by kaiservog on 25/02/2016.
 */
public class QuestionAddPackage extends RequestPackage{
    private String topic;
    private String question;

    public QuestionAddPackage(String token, String loginService, String topic, String question) {
        super(token, loginService);
        this.topic = topic;
        this.question = question;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
