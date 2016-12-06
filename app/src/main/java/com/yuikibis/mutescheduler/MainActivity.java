package com.yuikibis.mutescheduler;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.kobakei.ratethisapp.RateThisApp;
import com.yuikibis.mutescheduler.common.FragmentRouter;
import org.androidannotations.annotations.EActivity;

@EActivity
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        FragmentRouter.replace(getFragmentManager(), R.id.fragmentContainer, FragmentRouter.Tag.MAIN, null, false);
    }

    @Override
    protected void onStart() {
        super.onStart();

        RateThisApp.onStart(this);
        RateThisApp.showRateDialogIfNeeded(this);
    }

    // バックボタンを押した時に前のフラグメントに戻れるように。
    @Override
    public void onBackPressed() {
        FragmentManager manager = getFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            manager.popBackStack();
            return;
        }
        super.onBackPressed();
    }
}
