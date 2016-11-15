package com.example.cassel.ct_keep.helpers;

/**
 * Classe correspondente a um item de uma lista
 */

public class ListViewItem {

    private String texto_data;
    private String texto_tipo;
    private String texto_anexo;
    private String texto_andar;


    /**
     * Método construtor com apenas texto_data no item
     *
     * @param texto string referente ao item da lista
     */
    public ListViewItem(String texto) {
        this.setTexto_data(texto);
        this.setTexto_tipo("");
        this.setTexto_anexo("");
        this.setTexto_andar("");
    }

    public ListViewItem(String data, String tipo, String anexo, String andar) {
        this.setTexto_data(data);
        this.setTexto_tipo(tipo);
        this.setTexto_anexo(anexo);
        this.setTexto_andar(andar);
    }

    /**
     * GETTERS and SETTERS
     */

    public String getTexto_data() {
        return texto_data;
    }

    public void setTexto_data(String texto_data) {
        this.texto_data = texto_data;
    }

    public String getTexto_tipo() {
        return texto_tipo;
    }

    public void setTexto_tipo(String texto_tipo) {
        this.texto_tipo = texto_tipo;
    }

    public String getTexto_anexo() {
        return texto_anexo;
    }

    public void setTexto_anexo(String texto_anexo) {
        this.texto_anexo = texto_anexo;
    }

    public String getTexto_andar() {
        return texto_andar;
    }

    public void setTexto_andar(String texto_andar) {
        this.texto_andar = texto_andar;
    }
}
