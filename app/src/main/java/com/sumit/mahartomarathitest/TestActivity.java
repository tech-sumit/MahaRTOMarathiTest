/*
 * Created by, Sumit Shailendra Agrawal
 * Copyrights reserved at Sumit Shailendra Agrawal
 * ************Making World easy************
 */

package com.sumit.mahartomarathitest;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;
import com.sumit.mahartomarathitest.backbone.Question;
import com.sumit.mahartomarathitest.backbone.QuestionBank;

import java.io.InputStream;
import java.util.ArrayList;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView question, timer, questionNo;

    private Button submit;
    private RadioButton choice_1, choice_2, choice_3;
    private RadioGroup radioGroup;
    private ImageView imageView;
    private AlertDialog.Builder alertDialog;
    private AlertDialog dialog;
    private int count = 0, score = 0, q_no = 0, q_count = 0;
    private String answer = "";
    private String item_selected = "";
    private QuestionBank questionBank;
    private ArrayList<Question> questions;
    private Question questionObject;
    private int id = 0;
    private String question_no[] = {"०.", "१.", "२.", "३.", "४.", "५.", "६.", "७.", "८.", "९.", "१०.", "११.", "१२.", "१३.", "१४.", "१५."};
    private int total_questions = 10;
    private int passing_score = 6;
    private int questionCount = 0;
    private CountDownTimer countDownTimer;
    private int total_time = 10000;
    private FirebaseAnalytics mFirebaseAnalytics;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        showStart();

        mInterstitialAd= new InterstitialAd(TestActivity.this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        submit = (Button) findViewById(R.id.submit);
        questionNo = (TextView) findViewById(R.id.q_no);
        choice_1 = (RadioButton) findViewById(R.id.choice_1);
        choice_2 = (RadioButton) findViewById(R.id.choice_2);
        choice_3 = (RadioButton) findViewById(R.id.choice_3);
        question = (TextView) findViewById(R.id.question);
        timer = (TextView) findViewById(R.id.timer);
        imageView = (ImageView) findViewById(R.id.imageView);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        choice_1.setOnClickListener(this);
        choice_2.setOnClickListener(this);
        choice_3.setOnClickListener(this);
        submit.setOnClickListener(this);

        timer.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);
        questionNo.setVisibility(View.GONE);
        submit.setVisibility(View.GONE);
        question.setVisibility(View.GONE);
        choice_1.setVisibility(View.GONE);
        choice_2.setVisibility(View.GONE);
        choice_3.setVisibility(View.GONE);
    }

    private void showStart() {
        alertDialog = new AlertDialog.Builder(this);
        alertDialog.setView(R.layout.alert_layout);
        alertDialog.setCancelable(false);
        dialog = alertDialog.create();
        dialog.show();
    }

    public void startExam(View view) {
        if (dialog.isShowing()) {
            dialog.cancel();
        }
        questionBank = new QuestionBank(this, total_questions);
        putQuestion();
        timer.setVisibility(View.VISIBLE);
        questionNo.setVisibility(View.VISIBLE);
        submit.setVisibility(View.VISIBLE);
        question.setVisibility(View.VISIBLE);
        choice_1.setVisibility(View.VISIBLE);
        choice_2.setVisibility(View.VISIBLE);
        choice_3.setVisibility(View.VISIBLE);
    }

    private void putQuestion() {
        q_no++;
        if (q_no <= 10) {
            startCountdown();
            setQuestions();
            radioGroup.clearCheck();
            submit.setEnabled(true);
            submit.setBackgroundColor(Color.argb(255, 251, 192, 45));
        } else {
            generateResult();
        }
    }

    private void generateResult() {
        String message = "";
        if (score < passing_score) {
            message = "गुण:" + score + "\nनिकाल: नापास";
        } else {
            message = "गुण:" + score + "\nनिकाल: पास";
        }

        if (mFirebaseAnalytics != null) {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "score");
            mFirebaseAnalytics.logEvent("result", bundle);
            mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);
            mFirebaseAnalytics.setSessionTimeoutDuration(120000);
            mFirebaseAnalytics.setUserProperty("final_score", "Score: " + score);
        }
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setCancelable(false);

        View view = View.inflate(this, R.layout.result_layout, null);
        TextView message_view = (TextView) view.findViewById(R.id.resultMessage);
        message_view.setText(message);
        alertDialog.setView(view);
        dialog = alertDialog.create();
        Button buttonEndExam = (Button) view.findViewById(R.id.buttonEndExam);
        buttonEndExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInterstitialAd.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        long downTime = SystemClock.uptimeMillis();
                        long eventTime = SystemClock.uptimeMillis() + 500;
                        float x=getWindowManager().getDefaultDisplay().getWidth()/2;
                        float y=getWindowManager().getDefaultDisplay().getHeight()/2;
                        int metaState = 0;
                        MotionEvent motionEvent = MotionEvent.obtain(
                                downTime,
                                eventTime,
                                MotionEvent.ACTION_UP,
                                x,
                                y,
                                metaState
                        );

                        dispatchTouchEvent(motionEvent);
                    }
                },1000);
                dialog.cancel();
                onBackPressed();
            }
        });
        if (!isFinishing()) {
            dialog.show();
        }

    }

    public void endExam(View view) {
        onBackPressed();
    }

    private void startCountdown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer.start();
        } else {
            countDownTimer = new CountDownTimer(total_time, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timer.setText("वेळ बाकी:" + ((int) (millisUntilFinished / 1000)));
                }

                @Override
                public void onFinish() {
                    putQuestion();
                }
            }.start();
        }
    }

    private void setQuestions() {
        if (questionObject != null) {
            questionObject.reset();
            imageView.setVisibility(View.GONE);
        }
        if (questions == null) {
            questions = questionBank.getRandomQuestions();
        } else {
            if (questions.isEmpty()) {
                questions = questionBank.getRandomQuestions();
            }
        }
        questionObject = questions.get(q_count);
        count++;
        q_count++;
        String question_text = questionObject.getQuestion();
        String option1_text = questionObject.getOption1();
        String option2_text = questionObject.getOption2();
        String option3_text = questionObject.getOption3();
        answer = questionObject.getAnswer();
        if (question_text == "" || option1_text == "" || option2_text == "" || option3_text == "" || answer == "") {
            Log.e("ERROR", "Empty Question object recived");
            questionObject = questions.get(q_count);
            q_count++;
            question_text = questionObject.getQuestion();
            option1_text = questionObject.getOption1();
            option2_text = questionObject.getOption2();
            option3_text = questionObject.getOption3();
        }
        if (!questionObject.getImage().equals("")) {
            try {
                InputStream inputStream = getAssets().open("icons/" + questionObject.getImage());
                Drawable drawable = Drawable.createFromStream(inputStream, null);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageDrawable(drawable);
                inputStream.close();
            } catch (Exception e) {
                imageView.setImageBitmap(null);
                e.printStackTrace();
            }
        } else {
            Log.i("ERROR:", "Empty image tag exists in questions_db.xml");
        }


        choice_1.setTextColor(Color.BLACK);
        choice_2.setTextColor(Color.BLACK);
        choice_3.setTextColor(Color.BLACK);
        choice_1.setBackgroundColor(Color.WHITE);
        choice_2.setBackgroundColor(Color.WHITE);
        choice_3.setBackgroundColor(Color.WHITE);
        question.setText("" + question_text);
        choice_1.setText("" + option1_text);
        choice_2.setText("" + option2_text);
        choice_3.setText("" + option3_text);

        questionCount++;
        questionNo.setText("" + question_no[questionCount]);


        if (answer.equals("" + option1_text)) {
            id = choice_1.getId();
        } else if (answer.equals("" + option2_text)) {
            id = choice_2.getId();
        } else if (answer.equals("" + option3_text)) {
            id = choice_3.getId();
        }
    }

    @Override
    public void onClick(View v) {
        try {
            if (v.getId() == R.id.choice_1) {
                item_selected = choice_1.getText().toString();
            } else if (v.getId() == R.id.choice_2) {
                item_selected = choice_2.getText().toString();
            } else if (v.getId() == R.id.choice_3) {
                item_selected = choice_3.getText().toString();
            } else if (v.getId() == R.id.submit) {
                if (choice_1.isChecked() || choice_2.isChecked() || choice_3.isChecked()) {
                    submit.setEnabled(false);
                    submit.setBackgroundColor(Color.argb(255, 255, 213, 79));
                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                    }
                    int sel_id = radioGroup.getCheckedRadioButtonId();

                    RadioButton temp = (RadioButton) findViewById(sel_id);
                    if (id != 0) {
                        RadioButton temp_ans = (RadioButton) findViewById(id);
                        if (id == sel_id) {
                            score++;
                            temp.setTextColor(Color.WHITE);
                            temp_ans.setBackgroundColor(Color.argb(255, 100, 221, 23));//GREEN
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    putQuestion();
                                }
                            }, 4000);
                        } else {
                            temp.setTextColor(Color.WHITE);
                            temp_ans.setTextColor(Color.WHITE);
                            temp.setBackgroundColor(Color.argb(255, 239, 83, 80));//RED
                            temp_ans.setBackgroundColor(Color.argb(255, 100, 221, 23));//GREEN
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    putQuestion();
                                }
                            }, 4000);
                        }
                    }
                } else {
                    Toast.makeText(this, "कृपया पर्याय निवडा", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrash.logcat(Log.ERROR, "TestActivity", "Error in OnClickListener");
            FirebaseCrash.report(e);

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Bundle bundle = new Bundle();
        bundle.putCharSequence("text", questionObject.getQuestion());
        bundle.putCharSequence("option1", questionObject.getOption1());
        bundle.putCharSequence("option2", questionObject.getOption2());
        bundle.putCharSequence("option3", questionObject.getOption3());
        bundle.putCharSequence("answer", questionObject.getAnswer());
        bundle.putCharSequence("image", questionObject.getImage());
        outState.putAll(bundle);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        questionObject.setQuestion(savedInstanceState.getString("text"));
        questionObject.setOption1(savedInstanceState.getString("option1"));
        questionObject.setOption2(savedInstanceState.getString("option2"));
        questionObject.setOption3(savedInstanceState.getString("option3"));
        questionObject.setAnswer(savedInstanceState.getString("answer"));
        questionObject.setImage(savedInstanceState.getString("image"));
    }
}
