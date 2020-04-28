/*
 * Created by, Sumit Shailendra Agrawal
 * Copyrights reserved at Sumit Shailendra Agrawal
 * ************Making World easy************
 */
package com.sumit.mahartomarathitest.backbone;

import android.content.Context;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class DataRetriver {
    private ArrayList<Question> questions = new ArrayList<Question>();
    private Question question;
    private String text;
    private Context context;
    private int count = 0;

    public DataRetriver(Context context) {
        this.context = context;
    }

    public ArrayList<Question> parseXml() {
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open("questions_db.xml");
        } catch (IOException e) {
            e.printStackTrace();
            FirebaseCrash.logcat(Log.ERROR, "DataRetriver", "Opening xml file");
            FirebaseCrash.report(e);
        }
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            XmlPullParser parser = factory.newPullParser();

            parser.setInput(inputStream, null);

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("question")) {
                            question = new Question();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text = parser.getText().trim();
                        break;
                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("question")) {
                            questions.add(question);
                            count++;
                        } else if (tagname.equalsIgnoreCase("text")) {
                            question.setQuestion(text);
                        } else if (tagname.equalsIgnoreCase("option1")) {
                            question.setOption1(text);
                        } else if (tagname.equalsIgnoreCase("option2")) {
                            question.setOption2(text);
                        } else if (tagname.equalsIgnoreCase("option3")) {
                            question.setOption3(text);
                        } else if (tagname.equalsIgnoreCase("answer")) {
                            question.setAnswer(text);
                        } else if (tagname.equalsIgnoreCase("image")) {
                            question.setImage(text);
                        }
                        break;
                    default:
                        Log.e("ERROR", "Unknown event occured in switch case");
                        break;
                }
                eventType = parser.next();
                Log.i("Count", "\n" + count);
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
            FirebaseCrash.logcat(Log.ERROR, "DataRetriver", "XML Parser");
            FirebaseCrash.report(e);
        }
        return questions;
    }
}
