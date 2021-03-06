package com.example.cassel.ct_keep.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cassel.ct_keep.R;

import java.util.ArrayList;

/**
 * Class gerenciadora de uma List View
 */
public class ListViewAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<ListViewItem> itens;

    /**
     * Inicia a List View
     *
     * @param context
     * @param itens
     */
    public ListViewAdapter(Context context, ArrayList<ListViewItem> itens) {
        this.itens = itens;
        mInflater = LayoutInflater.from(context);
    }

    /**
     * Retorna a quantidade de itens
     *
     * @return Integer
     */
    public int getCount() {
        return itens.size();
    }

    /**
     * Retorna o item de acordo com a posicao dele na tela.
     *
     * @param position posição do item
     * @return ListViewItem
     */
    public ListViewItem getItem(int position) {
        return itens.get(position);
    }

    /**
     * Retorno o primeiro Item da List View
     *
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * Define os dados em suas determinadas posições na view
     *
     * @param position
     * @param view
     * @param parent
     * @return
     */
    public View getView(int position, View view, ViewGroup parent) {
        ListViewItem item = itens.get(position);
        view = mInflater.inflate(R.layout.item_list_view, null);
        ((TextView) view.findViewById(R.id.text_data)).setText(item.getTexto_data());
        ((TextView) view.findViewById(R.id.text_tipo)).setText(item.getTexto_tipo());
        ((TextView) view.findViewById(R.id.text_andar)).setText(item.getTexto_andar());
        ((TextView) view.findViewById(R.id.text_anexo)).setText(item.getTexto_anexo());
        return view;
    }
}
