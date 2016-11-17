package com.example.cassel.ct_keep.activities;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
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
import java.sql.Date;
import java.text.DateFormat;
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
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Activity do Formulário de Banheiros
 */
public class FormBanheiroActivity extends FormActivity {

    public static final MediaType FORM_DATA_TYPE
            = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    //Definição do endereço dos dados do Formulário google
    //Endereço do Form
    public static final String URL =
            "https://docs.google.com/forms/d/1qjB02HWVRXmMZ_yviNT2Oc0tBLMI4zYxywsQIDmah1U/formResponse";

    //Elementos de entrada do formulário
    public static final String EMAIL = "entry.682450967";
    public static final String SALA = "entry.316324308";
    public static final String ANDAR = "entry.856988063";
    public static final String ANEXO = "entry.294284294";
    public static final String TIPO = "entry.2109719163";
    public static final String LIMPEZA = "entry.1509520289";
    public static final String HIGIENICO = "entry.849708709";
    public static final String TOALHA = "entry.1615852955";
    public static final String SABONETE = "entry.1282061169";
    public static final String OUTRO = "entry.1001345839";

    //UI Helpers
    private Context context;

    //Informações do banheiro
    private String sala;
    private String andar;
    private String anexo;
    private String tipo;

    //Informações de solicitação
    private Switch limpeza;
    private Switch higienico;
    private Switch toalha;
    private Switch sabonete;
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
        setContentView(R.layout.activity_banheiro);

        context = this;

        getSalaInformation(getIntent().getExtras());
        defineInformation();
        defineElementsUI();

        Button sendButton = (Button) findViewById(R.id.enviaFormBanheiro);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog(getResources().getString(R.string.enviando_solicitação));
                PostDataTask postDataTask = new PostDataTask();
                String limpezaMsg = verifyChecked(limpeza.isChecked());
                String higienicoMsg = verifyChecked(higienico.isChecked());
                String toalhaMsg = verifyChecked(toalha.isChecked());
                String saboneteMSg = verifyChecked(sabonete.isChecked());
                String outroMsg = outro.getText().toString();

                postDataTask.execute(URL, sala, andar, anexo, tipo, limpezaMsg, higienicoMsg, toalhaMsg, saboneteMSg, outroMsg);
            }
        });
    }

    /**
     * Recebe informações da sala
     *
     * @param extras
     */
    public void getSalaInformation(Bundle extras) {
        if (extras != null) {
            sala = extras.getString("num");
            andar = extras.getString("andar") + "º Andar";
            anexo = extras.getString("anexo");
            tipo = extras.getString("tipo");
        } else {
            Toast.makeText(context, getResources().getString(R.string.erro_enviar), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    /**
     * Define as informações na Activity sobre a sala
     */
    public void defineInformation() {
        TextView salaId = (TextView) findViewById(R.id.textViewNumero);
        salaId.setText(sala);
        TextView andarId = (TextView) findViewById(R.id.textViewAndar);
        andarId.setText(andar);
        TextView anexoId = (TextView) findViewById(R.id.textViewAnexo);
        anexoId.setText(anexo);
        TextView tipoId = (TextView) findViewById(R.id.textViewTipo);
        tipoId.setText(tipo);
    }

    /**
     * Define quais são os elementos da View
     */
    public void defineElementsUI() {
        limpeza = (Switch) findViewById(R.id.switchLimpeza);
        higienico = (Switch) findViewById(R.id.switchHigienico);
        toalha = (Switch) findViewById(R.id.switchPapel);
        sabonete = (Switch) findViewById(R.id.switchSabonete);
        outro = (EditText) findViewById(R.id.editText);
    }

    /**
     * Cria uma tarefa Assincrona
     */
    private class PostDataTask extends AsyncTask<String, Void, Boolean> {
        /**
         * Efetua o envio dos dados em Background
         * Recebe os dados preenchidos pelo usuário e envia ao formulário
         */
        @Override
        protected Boolean doInBackground(String... contactData) {
            Boolean result = true;
            String email = getEmail(context);
            String url = contactData[0];
            String sala = contactData[1];
            String andar = contactData[2];
            String anexo = contactData[3];
            String tipo = contactData[4];
            String limpeza = contactData[5];
            String higienico = contactData[6];
            String toalha = contactData[7];
            String sabonete = contactData[8];
            String outro = contactData[9];
            String postBody = "";

            try {
                postBody = EMAIL + "=" + URLEncoder.encode(email, "UTF-8") +
                        "&" + SALA + "=" + URLEncoder.encode(sala, "UTF-8") +
                        "&" + ANDAR + "=" + URLEncoder.encode(andar, "UTF-8") +
                        "&" + ANEXO + "=" + URLEncoder.encode(anexo, "UTF-8") +
                        "&" + TIPO + "=" + URLEncoder.encode(tipo, "UTF-8") +
                        "&" + LIMPEZA + "=" + URLEncoder.encode(limpeza, "UTF-8") +
                        "&" + HIGIENICO + "=" + URLEncoder.encode(higienico, "UTF-8") +
                        "&" + TOALHA + "=" + URLEncoder.encode(toalha, "UTF-8") +
                        "&" + SABONETE + "=" + URLEncoder.encode(sabonete, "UTF-8") +
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
                Response response = client.newCall(request).execute();
            } catch (IOException exception) {
                result = false;
            }
            return result;
        }

        /**
         * Executa ao completar o envio dos daoos
         *
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
