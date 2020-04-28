/*
 * Created by, Sumit Shailendra Agrawal
 * Copyrights reserved at Sumit Shailendra Agrawal
 * ************Making World easy************
 */
package com.sumit.mahartomarathitest.backbone;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;

public class QuestionBank {
    private Context context;
    private DataRetriver dataRetriver;
    private ArrayList<Question> arrayList;
    private Question question;
    private int question_count = 0;

    public QuestionBank(Context context, int question_count) {
        this.context = context;
        this.question_count = question_count;
        dataRetriver = new DataRetriver(context);
        arrayList = dataRetriver.parseXml();
    }

    public ArrayList<Question> getRandomQuestions() {
        ArrayList<Question> tempList = new ArrayList<>();
        Collections.shuffle(arrayList);
        for (int i = 0; i < question_count; i++) {
            tempList.add(arrayList.get(i));
        }
        return tempList;
    }

    public boolean checkAnswer(Question question, String answer) {
        return question.getAnswer().equalsIgnoreCase(answer);
    }
}