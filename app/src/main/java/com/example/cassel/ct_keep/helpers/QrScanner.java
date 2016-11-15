package com.example.cassel.ct_keep.helpers;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.cassel.ct_keep.activities.BanheiroActivity;
import com.example.cassel.ct_keep.activities.SalaActivity;
import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONObject;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QrScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    JSONArray sala = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();   // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        String jsonStr = rawResult.getText();
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            sala = jsonObj.getJSONArray("sala");
            JSONObject c = sala.getJSONObject(0);
            Integer tipo = Integer.parseInt(c.getString("tipo").toString());
            String numero = c.getString("numero").toString();
            String banheiro = c.getString("banheiro").toString();
            String anexo = c.getString("anexo").toString();
            String andar = c.getString("andar").toString();
            sendToView(tipo, numero, banheiro, anexo, andar);
        } catch (Exception e) {
            Toast.makeText(this, "Não foi possível identificar o QRCode, tente novamente!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            //mScannerView.resumeCameraPreview(this);
            this.finish();
        }
    }

    public void sendToView(Integer tipo, String numero, String banheiro, String anexo, String andar) {
        Intent intent;
        if (tipo == 1) {
            intent = new Intent(this, BanheiroActivity.class);
        } else {
            intent = new Intent(this, SalaActivity.class);
        }
        intent.putExtra("num", numero);
        intent.putExtra("tipo", banheiro);
        intent.putExtra("anexo", anexo);
        intent.putExtra("andar", andar);
        startActivity(intent);
        this.finish();
    }

}

//

// Log.e("handler", rawResult.getText()); // Prints scan results
// If you would like to resume scanning, call this method below:
// mScannerView.resumeCameraPreview(this);