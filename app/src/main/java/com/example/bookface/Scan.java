package com.example.bookface;


import android.app.Activity;

import com.google.zxing.integration.android.IntentIntegrator;

/**
 * This is a class that is used to scan the book
 */
public class Scan extends Activity {
    // Declare variables
    private String isbnCode;
    private Activity currentActivity;

    /**
     * This is the constructor
     * @param currentActivity
     */
    public Scan(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    /**
     * This is the method that scans the book
     */
    public void scanCode() {
        IntentIntegrator integator = new IntentIntegrator(currentActivity);
        integator.setCaptureActivity(CaptureAct.class);
        integator.setOrientationLocked(false);
        integator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integator.setPrompt("Scanning ISBN..");
        integator.initiateScan();
    }
}
