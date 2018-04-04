package br.com.senaijandira.kingsmoney.model;

import android.graphics.Bitmap;

public class EstatisticaCategoria {
    private String nome;
    private double valor;
    private double porcentagem;
    private Bitmap foto;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public double getPorcentagem() {
        if (Double.isNaN(this.porcentagem)) {
            return 0;
        }

        return porcentagem;
    }

    public void setPorcentagem(double porcentagem) {
        this.porcentagem = porcentagem;
    }

    public Bitmap getFoto() {
        return foto;
    }

    public void setFoto(Bitmap foto) {
        this.foto = foto;
    }
}