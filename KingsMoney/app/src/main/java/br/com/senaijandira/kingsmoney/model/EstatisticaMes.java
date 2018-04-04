package br.com.senaijandira.kingsmoney.model;

import java.util.Calendar;
import java.util.Date;

public class EstatisticaMes {
    private Date data;
    private double valor;
    private double porcentagem;
    private Calendar calendar;

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
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

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }
}