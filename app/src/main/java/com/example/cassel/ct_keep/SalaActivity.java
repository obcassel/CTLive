package com.example.cassel.ct_keep;


import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.accounts.Account;
import android.accounts.AccountManager;


public class SalaActivity extends AppCompatActivity {

    public static final MediaType FORM_DATA_TYPE
            = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    public static final String URL =
            "https://docs.google.com/forms/d/1qUOfoFnKj1Ndm6zHb54iKOkRinFrBrmDaK0K4Utyr8Y/formResponse";
    //FORM
    public static final String EMAIL = "entry.961485963";
    public static final String SALA = "entry.1101090603";
    public static final String ANDAR = "entry.1427981111";
    public static final String ANEXO = "entry.1256369507";
    public static final String LIMPEZA = "entry.1411528981";
    public static final String MATERIAL = "entry.959522438";
    public static final String ARCONDICIONADO = "entry.2112128155";
    public static final String DATASHOW = "entry.483534463";
    public static final String OUTRO = "entry.879391715";

    //UI Helpers
    private Context context;

    private String sala;
    private String andar;
    private String anexo;

    private Switch limpeza;
    private Switch material;
    private Switch arcondicionado;
    private Switch datashow;
    private EditText outro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sala);

        //Contexto da Activity
        context = this;

        getSalaInformation(getIntent().getExtras());
        defineInformation();
        defineElementsUI();

        Button sendButton = (Button) findViewById(R.id.enviaFormSala);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostDataTask postDataTask = new PostDataTask();

                String limpezaMsg = verifyChecked(limpeza.isChecked());
                String materialMsg = verifyChecked(material.isChecked());
                String arMsg = verifyChecked(arcondicionado.isChecked());
                String datashowMSg = verifyChecked(datashow.isChecked());
                String outroMsg = outro.getText().toString();

                postDataTask.execute(URL, sala, andar, anexo, limpezaMsg, materialMsg, arMsg, datashowMSg, outroMsg);
            }
        });
    }

    //Get Sala Information
    public void getSalaInformation(Bundle extras) {
        if (extras != null) {
            sala = extras.getString("num");
            andar = extras.getString("andar")  + "º Andar";
            anexo = extras.getString("anexo");
        } else {
            Toast.makeText(context, "Um erro ocorreu, por favor, tente novamente!", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    //Define as informações na Activity sobre a sala
    public void defineInformation() {
        TextView salaId = (TextView)findViewById(R.id.textViewNumero);
        salaId.setText(sala);
        TextView andarId = (TextView)findViewById(R.id.textViewAndar);
        andarId.setText(andar);
        TextView anexoId = (TextView)findViewById(R.id.textViewAnexo);
        anexoId.setText(anexo);
    }

    //Define UI Elements
    public void defineElementsUI() {
        limpeza = (Switch) findViewById(R.id.switchLimpeza);
        material = (Switch) findViewById(R.id.switchMateriais);
        arcondicionado = (Switch) findViewById(R.id.switchArQuebrado);
        datashow = (Switch) findViewById(R.id.switchDataShowQuebrado);
        outro = (EditText) findViewById(R.id.editText);
    }

    //Verifica se esta checado
    public String verifyChecked(boolean isChecked) {
        if (isChecked) {
            return "SIM";
        } else {
            return "";
        }
    }

    static String getEmail(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account account = getAccount(accountManager);

        if (account == null) {
            return null;
        } else {
            return account.name;
        }
    }

    private static Account getAccount(AccountManager accountManager) {
        Account[] accounts = accountManager.getAccountsByType("com.google");
        Account account;
        if (accounts.length > 0) {
            account = accounts[0];
        } else {
            account = null;
        }
        return account;
    }

    private class PostDataTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... contactData) {
            Boolean result = true;
            String email = getEmail(context);
            String url = contactData[0];
            String sala = contactData[1];
            String andar = contactData[2];
            String anexo = contactData[3];
            String limpeza = contactData[4];
            String material = contactData[5];
            String ar = contactData[6];
            String datashow = contactData[7];
            String outro = contactData[8];
            String postBody = "";

            try {
                postBody = EMAIL + "=" + URLEncoder.encode(email, "UTF-8") +
                        "&" +  SALA + "=" + URLEncoder.encode(sala, "UTF-8") +
                        "&" + ANDAR + "=" + URLEncoder.encode(andar, "UTF-8") +
                        "&" + ANEXO + "=" + URLEncoder.encode(anexo, "UTF-8") +
                        "&" + LIMPEZA + "=" + URLEncoder.encode(limpeza, "UTF-8") +
                        "&" + MATERIAL + "=" + URLEncoder.encode(material, "UTF-8") +
                        "&" + ARCONDICIONADO + "=" + URLEncoder.encode(ar, "UTF-8") +
                        "&" + DATASHOW + "=" + URLEncoder.encode(datashow, "UTF-8") +
                        "&" + OUTRO + "=" + URLEncoder.encode(outro, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                result = false;
            }
            try {
                OkHttpClient client = new OkHttpClient();
                RequestBody body = RequestBody.create(FORM_DATA_TYPE, postBody);
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                //Send the request
                Response response = client.newCall(request).execute();
            } catch (IOException exception) {
                result = false;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            Toast.makeText(context, result ? "Solicitação enviada com sucesso!" : "Ocorreu um erro ao enviar a solicitação. Por favor, verifique a conexão com a internet.", Toast.LENGTH_LONG).show();
            finish();
        }

    }
}
