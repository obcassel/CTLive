package com.example.cassel.ct_keep.helpers;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.cassel.ct_keep.R;
import com.example.cassel.ct_keep.activities.FormBanheiroActivity;
import com.example.cassel.ct_keep.activities.FormSalaActivity;
import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONObject;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * QRCode Scanner
 */
public class QrScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    JSONArray sala = null;

    /**
     * Método construtor
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    /**
     * Stop camera on pause
     */
    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    /**
     * Gerenciador dos dados lidos através do QRCode
     * Efetua a leitura de um JSON
     *
     * @param rawResult
     */
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
            String serial = c.getString("serial").toString();
            if (checkCRC(tipo, numero, banheiro, anexo, andar, serial)) {
                sendToView(tipo, numero, banheiro, anexo, andar);
            } else {
                Toast.makeText(this, getResources().getString(R.string.qrcode_invalido), Toast.LENGTH_LONG).show();
                this.finish();
            }
        } catch (Exception e) {
            Toast.makeText(this, getResources().getString(R.string.qrcode_nao_identificado) , Toast.LENGTH_LONG).show();
            e.printStackTrace();
            this.finish();
        }
    }

    /**
     * Prepara os dados para verificação do serial/crc
     *
     * @param tipo     Tipo de sala
     * @param numero   Numero da sala
     * @param banheiro Se banheiro, feminino ou masculino
     * @param anexo    Anexo da sala
     * @param andar    Andar da sala
     * @param serial   Numero serial/crc gerado pelo gerenciador de QRCodes
     * @return boolean Retorna se o CRC está de acordo com os dados
     */
    public boolean checkCRC(Integer tipo, String numero, String banheiro, String anexo, String andar, String serial) {
        SrcManager src = new SrcManager();
        String str = tipo.toString() + numero + banheiro + andar + anexo;
        Integer srcSerial = src.calcularCRC(str);
        if (Integer.parseInt(serial) == srcSerial) {
            return true;
        }
        return false;
    }

    /**
     * Envia os dados para a view
     * Definindo se é do tipo banheiro ou sala
     *
     * @param tipo     Tipo de sala
     * @param numero   Numero da sala
     * @param banheiro Se banheiro, feminino ou masculino
     * @param anexo    Anexo da sala
     * @param andar    Andar da sala
     */
    public void sendToView(Integer tipo, String numero, String banheiro, String anexo, String andar) {
        Intent intent;
        if (tipo == 1) {
            intent = new Intent(this, FormBanheiroActivity.class);
        } else {
            intent = new Intent(this, FormSalaActivity.class);
        }
        intent.putExtra("num", numero);
        intent.putExtra("tipo", banheiro);
        intent.putExtra("anexo", anexo);
        intent.putExtra("andar", andar);
        startActivity(intent);
        this.finish();
    }
}