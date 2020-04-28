/*
 * Created by, Sumit Shailendra Agrawal
 * Copyrights reserved at Sumit Shailendra Agrawal
 * ************Making World easy************
 */

package com.sumit.mahartomarathitest.backbone;

public class Question {
    private int id;
    private String question = "";
    private String option1 = "";
    private String option2 = "";
    private String option3 = "";
    private String answer = "";
    private String image = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String text) {
        this.question = text;
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

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void reset() {
        id = 0;
        question = "";
        option1 = "";
        option2 = "";
        option3 = "";
        answer = "";
        image = "";
    }
}
