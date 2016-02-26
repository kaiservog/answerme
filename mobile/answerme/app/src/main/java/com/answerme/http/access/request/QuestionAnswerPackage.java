package com.answerme.http.access.request;

/**
 * Created by kaiservog on 25/02/2016.
 */
public class QuestionAnswerPackage extends RequestPackage {
    private String questionId;
    private String answer;

    public QuestionAnswerPackage(String token, String loginService, String questionId, String answer) {
        super(token, loginService);
        this.questionId = questionId;
        this.answer = answer;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
