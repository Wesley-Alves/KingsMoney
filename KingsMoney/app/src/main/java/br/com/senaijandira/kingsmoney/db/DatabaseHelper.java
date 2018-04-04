package br.com.senaijandira.kingsmoney.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.BitmapFactory;

import br.com.senaijandira.kingsmoney.R;
import br.com.senaijandira.kingsmoney.util.BitmapUtil;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DB_NAME = "kingsmoney.db";
    private static int DB_VERSION = 1;
    private Context context;

    public DatabaseHelper(Context ctx) {
        super(ctx, DB_NAME, null, DB_VERSION);
        context = ctx;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE tbl_categoria (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT NOT NULL);");

        db.execSQL("CREATE TABLE tbl_lancamento (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "valor REAL NOT NULL," +
                "tipo INTEGER NOT NULL," +
                "titulo TEXT NOT NULL," +
                "descricao TEXT NOT NULL," +
                "data INT NOT NULL," +
                "idCategoria INT NOT NULL," +
                "FOREIGN KEY(idCategoria) REFERENCES tbl_categoria(_id));");

        criarCategoria(db, "Lazer", R.drawable.icone_lazer);
        criarCategoria(db, "Saúde", R.drawable.icone_saude);
        criarCategoria(db, "Salário", R.drawable.ic_attach_money_black_24dp);
        criarCategoria(db, "Moradia", R.drawable.ic_home_black_24dp);
        criarCategoria(db, "Transporte", R.drawable.ic_directions_bus_black_24dp);
        criarCategoria(db, "Outros", R.drawable.ic_list_black_24dp);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE tbl_categoria;");
        db.execSQL("DROP TABLE tbl_lancamento;");
        onCreate(db);
    }

    private void criarCategoria(SQLiteDatabase db, String nome, int foto) {
        // Insere uma categoria no banco de dados e escreve a imagem nos dados do aplicativo.
        ContentValues valores = new ContentValues();
        valores.put("nome", nome);
        long id = db.insert("tbl_categoria", null, valores);
        BitmapUtil.getInstance().writeImage(context, id, BitmapFactory.decodeResource(context.getResources(), foto));
    }
}