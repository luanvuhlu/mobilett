package com.bigbear.mobilett;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by luanvu on 4/19/15.
 */
public class ScannerActivity extends Activity implements ZXingScannerView.ResultHandler {
    public static final String SCAN_RESULT_KEY="SCAN_RESULT";
    private ZXingScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(scannerView);
    }
    @Override
    public void onResume() {
        super.onResume();
        scannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        scannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();           // Stop camera on pause
    }
    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Intent intent = new Intent();
        Bundle conData = new Bundle();
        conData.putString(SCAN_RESULT_KEY, rawResult.getText());
        intent.putExtras(conData);
        setResult(RESULT_OK, intent);
        finish();
    }
}
