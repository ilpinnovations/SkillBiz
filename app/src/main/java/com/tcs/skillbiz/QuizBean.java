package com.tcs.skillbiz;

/**
 * Created by 966893 on 1/30/2016.
 */
public class QuizBean {

    String topicId, question,option1, option2, option3, option4;
    int answerOption;

    public QuizBean(String topicId, String question, String option1, String option2, String option3, String option4, int answerOption) {
        this.topicId = topicId;
        this.question = question;
        this.option2 = option2;
        this.option1 = option1;
        this.option3 = option3;
        this.option4 = option4;
        this.answerOption = answerOption;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public int getAnswerOption() {
        return answerOption;
    }

    public void setAnswerOption(int answerOption) {
        this.answerOption = answerOption;
    }
}
