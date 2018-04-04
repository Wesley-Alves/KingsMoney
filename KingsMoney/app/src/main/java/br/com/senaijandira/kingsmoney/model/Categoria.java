package br.com.senaijandira.kingsmoney.model;

import android.graphics.Bitmap;

public class Categoria {
    private int id;
    private String nome;
    private Bitmap foto;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Bitmap getFoto() {
        return foto;
    }

    public void setFoto(Bitmap foto) {
        this.foto = foto;
    }
}