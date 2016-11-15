package com.example.cassel.ct_keep.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.cassel.ct_keep.R;
import com.example.cassel.ct_keep.helpers.ListViewAdapter;
import com.example.cassel.ct_keep.helpers.ListViewItem;

import java.util.ArrayList;


public class HistoricActivity extends AppCompatActivity {
    private AbsListView lista;
    private ArrayList<ListViewItem> itens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic);

        itens = new ArrayList<>();
        lista = (ListView) findViewById(R.id.list_historic);

        this.searchHistoric(this);
    }

    public void searchHistoric(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("LivePreferences", Context.MODE_PRIVATE);
        Integer number = sharedPref.getInt("Number", 0);
        if(number == 0) {
            itens.add(new ListViewItem("Nenhuma solicitação encontrada"));
        } else {
            for (Integer i = 1; i <= number; i++) {
                itens.add(new ListViewItem(sharedPref.getString("data_" + i, ""),
                                           sharedPref.getString("tipo_" + i, ""),
                                           sharedPref.getString("anexo_" + i, ""),
                                           sharedPref.getString("andar_" + i, "")));
            }
        }
        setAdapter();
    }

    public void setAdapter() {
        ListViewAdapter listViewAdapter = new ListViewAdapter(this, itens);
        lista.setAdapter(listViewAdapter);
        lista.setCacheColorHint(Color.TRANSPARENT);
    }
}