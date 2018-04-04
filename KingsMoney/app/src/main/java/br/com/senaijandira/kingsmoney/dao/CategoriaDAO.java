package br.com.senaijandira.kingsmoney.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import br.com.senaijandira.kingsmoney.db.DatabaseHelper;
import br.com.senaijandira.kingsmoney.model.Categoria;
import br.com.senaijandira.kingsmoney.util.BitmapUtil;

public class CategoriaDAO {
    private static CategoriaDAO instance;

    public static CategoriaDAO getInstance() {
        if (instance == null) {
            instance = new CategoriaDAO();
        }

        return instance;
    }

    public void inserir(Context context, Categoria categoria) {
        SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("nome", categoria.getNome());
        long id = db.insert("tbl_categoria", null, valores);
        if (categoria.getFoto() != null) {
            BitmapUtil.getInstance().writeImage(context, id, categoria.getFoto());
        }
    }

    public ArrayList<Categoria> selecionarTodos(Context context) {
        ArrayList<Categoria> lista = new ArrayList<>();
        SQLiteDatabase db = new DatabaseHelper(context).getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _id, nome FROM tbl_categoria;", null);
        while (cursor.moveToNext()) {
            Categoria categoria = new Categoria();
            categoria.setId(cursor.getInt(0));
            categoria.setNome(cursor.getString(1));
            categoria.setFoto(BitmapUtil.getInstance().readImage(context, categoria.getId()));
            lista.add(categoria);
        }

        cursor.close();
        return lista;
    }

    public Categoria selecionarUm(Context context, int id) {
        Categoria categoria = null;
        SQLiteDatabase db = new DatabaseHelper(context).getReadableDatabase();
        String sql = "SELECT * FROM tbl_categoria WHERE _id = ?;";
        Cursor cursor = db.rawQuery(sql, new String[] {String.valueOf(id)});
        if (cursor.moveToNext()) {
            categoria = new Categoria();
            categoria.setId(cursor.getInt(0));
            categoria.setNome(cursor.getString(1));
            categoria.setFoto(BitmapUtil.getInstance().readImage(context, categoria.getId()));
        }

        cursor.close();
        return categoria;
    }

    public void remover(Context context, int id) {
        SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();
        db.delete("tbl_categoria", "_id = ?", new String[] {String.valueOf(id)});
    }

    public void atualizar(Context context, Categoria categoria) {
        SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("nome", categoria.getNome());
        if (categoria.getFoto() != null) {
            BitmapUtil.getInstance().writeImage(context, categoria.getId(), categoria.getFoto());
        }

        db.update("tbl_categoria", valores, "_id = ?", new String[] {String.valueOf(categoria.getId())});
    }
}