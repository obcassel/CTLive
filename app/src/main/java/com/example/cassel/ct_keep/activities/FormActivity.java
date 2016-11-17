package com.example.cassel.ct_keep.activities;


import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FormActivity extends AppCompatActivity {
    private ProgressDialog dialog;

    /**
     * Verifica se esta checado para envio ao formulário
     *
     * @param isChecked boolean Valor do CheckBox
     * @return String
     */
    public String verifyChecked(boolean isChecked) {
        if (isChecked) {
            return "SIM";
        } else {
            return "";
        }
    }

    /**
     * Busca o email do usuário que fez a solicitação
     *
     * @param context
     * @return
     */
    public String getEmail(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account account = getAccount(accountManager);
        if (account == null) {
            return null;
        } else {
            return account.name;
        }
    }

    /**
     * Retorna as informções da conta do usuário
     *
     * @param accountManager Gerenciador de Contas
     * @return Conta de usuário
     */
    private Account getAccount(AccountManager accountManager) {
        Account[] accounts = null;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED) {
            accounts = accountManager.getAccountsByType("com.google");
        }
        Account account;
        if (accounts.length > 0) {
            account = accounts[0];
        } else {
            account = null;
        }
        return account;
    }

    /**
     * Salva Historico de Solicitações
     */
    public void saveHistoricSolicitation(Context context, String anexo, String andar) {
        SharedPreferences sharedPref = context.getSharedPreferences("LivePreferences", Context.MODE_PRIVATE);
        int number = sharedPref.getInt("Number", 0);

        SharedPreferences.Editor editor = sharedPref.edit();

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        Integer i = number + 1;
        editor.putInt("Number", i);
        editor.putString("data_" + i, df.format(c.getTime()));
        editor.putString("tipo_" + i, "Banheiro");
        editor.putString("anexo_" + i, "Anexo " + anexo);
        editor.putString("andar_" + i, andar);
        editor.commit();
    }

    /**
     * Cria uma mensagem de carregamento na activity
     *
     * @param message mensagem a ser mostrada
     */
    public void createDialog(String message) {
        this.dialog = new ProgressDialog(this);
        this.dialog.setMessage(message);
        this.dialog.setIndeterminate(false);
        this.dialog.setCanceledOnTouchOutside(false);
        this.dialog.show();
    }

    /**
     * Remove a mensagem da activity
     */
    public void destroyDialog() {
        this.dialog.dismiss();
    }
}
