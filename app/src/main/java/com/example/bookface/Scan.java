package com.example.bookface;


import android.app.Activity;

import com.google.zxing.integration.android.IntentIntegrator;


public class Scan extends Activity{
    private String isbnCode;
    private Activity currentActivity;

    public Scan(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    public void scanCode() {
        IntentIntegrator integator = new IntentIntegrator(currentActivity);
        integator.setCaptureActivity(CaptureAct.class);
        integator.setOrientationLocked(false);
        integator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integator.setPrompt("Scanning ISBN..");
        integator.initiateScan();
    }
}
