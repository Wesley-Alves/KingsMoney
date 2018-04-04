package br.com.senaijandira.kingsmoney.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;

import br.com.senaijandira.kingsmoney.db.DatabaseHelper;
import br.com.senaijandira.kingsmoney.model.Lancamento;

public class LancamentoDAO {
    private static LancamentoDAO instance;

    public static LancamentoDAO getInstance() {
        if (instance == null) {
            instance = new LancamentoDAO();
        }

        return instance;
    }

    public void inserir(Context context, Lancamento lancamento) {
        SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("valor", lancamento.getValor());
        valores.put("tipo", lancamento.getTipo());
        valores.put("titulo", lancamento.getTitulo());
        valores.put("descricao", lancamento.getDescricao());
        valores.put("data", lancamento.getData().getTime());
        valores.put("idCategoria", lancamento.getIdCategoria());
        db.insert("tbl_lancamento", null, valores);
    }

    public ArrayList<Lancamento> selecionarTodos(Context context) {
        ArrayList<Lancamento> lista = new ArrayList<>();
        SQLiteDatabase db = new DatabaseHelper(context).getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM tbl_lancamento;", null);
        while (cursor.moveToNext()) {
            Lancamento lancamento = new Lancamento();
            lancamento.setId(cursor.getInt(0));
            lancamento.setValor(cursor.getDouble(1));
            lancamento.setTipo(cursor.getInt(2));
            lancamento.setTitulo(cursor.getString(3));
            lancamento.setDescricao(cursor.getString(4));
            lancamento.setData(new Date(cursor.getLong(5)));
            lancamento.setIdCategoria(cursor.getInt(6));
            lista.add(lancamento);
        }

        cursor.close();
        return lista;
    }

    public Lancamento selecionarUm(Context context, int id) {
        Lancamento lancamento = null;
        SQLiteDatabase db = new DatabaseHelper(context).getReadableDatabase();
        String sql = "SELECT * FROM tbl_lancamento WHERE _id = ?;";
        Cursor cursor = db.rawQuery(sql, new String[] {String.valueOf(id)});
        if (cursor.moveToNext()) {
            lancamento = new Lancamento();
            lancamento.setId(cursor.getInt(0));
            lancamento.setValor(cursor.getDouble(1));
            lancamento.setTipo(cursor.getInt(2));
            lancamento.setTitulo(cursor.getString(3));
            lancamento.setDescricao(cursor.getString(4));
            lancamento.setData(new Date(cursor.getLong(5)));
            lancamento.setIdCategoria(cursor.getInt(6));
        }

        cursor.close();
        return lancamento;
    }

    public boolean checarLancamentosByCategoria(Context context, int idCategoria) {
        SQLiteDatabase db = new DatabaseHelper(context).getReadableDatabase();
        String sql = "SELECT _id FROM tbl_lancamento WHERE idCategoria = ?;";
        Cursor cursor = db.rawQuery(sql, new String[] {String.valueOf(idCategoria)});
        boolean resultado = cursor.moveToNext();
        cursor.close();
        return resultado;
    }

    public void remover(Context context, int id) {
        SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();
        db.delete("tbl_lancamento", "_id = ?", new String[] {String.valueOf(id)});
    }

    public void atualizar(Context context, Lancamento lancamento) {
        SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("valor", lancamento.getValor());
        valores.put("tipo", lancamento.getTipo());
        valores.put("titulo", lancamento.getTitulo());
        valores.put("descricao", lancamento.getDescricao());
        valores.put("data", lancamento.getData().getTime());
        valores.put("idCategoria", lancamento.getIdCategoria());
        db.update("tbl_lancamento", valores, "_id = ?", new String[] {String.valueOf(lancamento.getId())});
    }

    public double[] getSaldoGeral(Context context) {
        // Seleciona as somas de despesas e receitas a partir do banco de dados
        double[] total = new double[] {0, 0};
        SQLiteDatabase db = new DatabaseHelper(context).getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM((tipo = 0) * valor) AS despesa, SUM((tipo = 1) * valor) AS receita FROM tbl_lancamento", null);
        if (cursor.moveToNext()) {
            total = new double[] {cursor.getDouble(0), cursor.getDouble(1)};
        }

        cursor.close();
        return total;
    }

    public double getValorByCategoria(Context context, int idCategoria) {
        // Pega a soma total de todas as despesas em uma categoria específica
        double total = 0;
        SQLiteDatabase db = new DatabaseHelper(context).getReadableDatabase();
        String sql = "SELECT SUM(valor) AS total FROM tbl_lancamento WHERE idCategoria = ? AND tipo = 0;";
        Cursor cursor = db.rawQuery(sql, new String[] {String.valueOf(idCategoria)});
        if (cursor.moveToNext()) {
            total = cursor.getDouble(0);
        }

        cursor.close();
        return total;
    }
}