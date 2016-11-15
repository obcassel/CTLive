package com.example.cassel.ct_keep.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.cassel.ct_keep.R;
import com.example.cassel.ct_keep.helpers.QrScanner;

import org.json.JSONArray;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity {

    private ZXingScannerView mScannerView;
    JSONArray sala = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //força orientação portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void QrScanner(View view) {
        Intent intent = new Intent(this, QrScanner.class);
        startActivity(intent);
    }

    public void howItWorks(View view) {
        Intent intent = new Intent(this, HowItWork.class);
        startActivity(intent);
    }

    public void viewHistoric(View view) {
        Intent intent = new Intent(this, HistoricActivity.class);
        startActivity(intent);
    }


}