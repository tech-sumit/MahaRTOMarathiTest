package com.sumit.mahartomarathitest;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class ContactUs extends AppCompatActivity {
    private EditText feedbackText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        feedbackText = (EditText) findViewById(R.id.feedback_text);
        Button sendFeedback = (Button) findViewById(R.id.send_feedback);
        MobileAds.initialize(this, getString(R.string.banner_ad_unit_id));
        AdView mAdView = (AdView) findViewById(R.id.adViewContact);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        sendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL, "easyconsserv@gmail.com");
                intent.putExtra(Intent.EXTRA_SUBJECT, "MahaRTO Marathi Test Report");
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_TEXT, "" + feedbackText.getText().toString());
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "No mail client installed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getFragmentManager().popBackStack();
    }
}
