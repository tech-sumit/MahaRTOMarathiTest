/*
 * Created by, Sumit Shailendra Agrawal
 * Copyrights reserved at Sumit Shailendra Agrawal
 * ************Making World easy************
 */

package com.sumit.mahartomarathitest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        MobileAds.initialize(this, getString(R.string.banner_ad_unit_id));
        AdView mAdView = (AdView) findViewById(R.id.adViewAbout);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
