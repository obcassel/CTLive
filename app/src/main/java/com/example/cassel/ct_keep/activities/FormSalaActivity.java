package com.example.cassel.ct_keep.activities;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.accounts.Account;
import android.accounts.AccountManager;

import com.example.cassel.ct_keep.R;

/**
 * Activity do Formulário de Salas
 */
public class FormSalaActivity extends FormActivity{

    public static final MediaType FORM_DATA_TYPE
            = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    //Definição do endereço dos dados do Formulário google
    //Endereço do Form
    public static final String URL =
            "https://docs.google.com/forms/d/1qUOfoFnKj1Ndm6zHb54iKOkRinFrBrmDaK0K4Utyr8Y/formResponse";
    //Elementos de entrada do formulário
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

    //Informações do banheiro
    private String sala;
    private String andar;
    private String anexo;

    //Informações de solicitação
    private Switch limpeza;
    private Switch material;
    private Switch arcondicionado;
    private Switch datashow;
    private EditText outro;

    /**
     * Método de criação da activity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sala);

        context = this;

        getSalaInformation(getIntent().getExtras());
        defineInformation();
        defineElementsUI();

        Button sendButton = (Button) findViewById(R.id.enviaFormSala);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog(getResources().getString(R.string.enviando_solicitação));
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

    /**
     * Recebe as Informações do banheiro
     *
     * @param extras
     */
    public void getSalaInformation(Bundle extras) {
        if (extras != null) {
            sala = extras.getString("num");
            andar = extras.getString("andar") + "º Andar";
            anexo = extras.getString("anexo");
        } else {
            Toast.makeText(context, getResources().getString(R.string.erro_enviar), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    /**
     * Define as informações na View sobre a sala
     */
    public void defineInformation() {
        TextView salaId = (TextView) findViewById(R.id.textViewNumero);
        salaId.setText(sala);
        TextView andarId = (TextView) findViewById(R.id.textViewAndar);
        andarId.setText(andar);
        TextView anexoId = (TextView) findViewById(R.id.textViewAnexo);
        anexoId.setText(anexo);
    }

    /**
     *  Define quais são os elementos da View
     */
    public void defineElementsUI() {
        limpeza = (Switch) findViewById(R.id.switchLimpeza);
        material = (Switch) findViewById(R.id.switchMateriais);
        arcondicionado = (Switch) findViewById(R.id.switchArQuebrado);
        datashow = (Switch) findViewById(R.id.switchDataShowQuebrado);
        outro = (EditText) findViewById(R.id.editText);
    }

    /**
     * Cria uma tarefa Assincrona
     */
    private class PostDataTask extends AsyncTask<String, Void, Boolean> {
        @Override
        /**
         * Efetua o envio dos dados em Background
         * Recebe os dados preenchidos pelo usuário e envia ao formulário
         */
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
                        "&" + SALA + "=" + URLEncoder.encode(sala, "UTF-8") +
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

        /**
         * Executa ao completar o envio dos daoos
         * @param result
         */
        @Override
        protected void onPostExecute(Boolean result) {
            saveHistoricSolicitation(context, anexo, andar);
            Toast.makeText(context, result ? getResources().getString(R.string.sucesso_enviar) : getResources().getString(R.string.erro_enviar), Toast.LENGTH_LONG).show();
            destroyDialog();
            finish();
        }
    }
}
