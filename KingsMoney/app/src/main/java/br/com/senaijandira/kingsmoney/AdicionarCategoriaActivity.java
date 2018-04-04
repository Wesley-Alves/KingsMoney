package br.com.senaijandira.kingsmoney;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.FileNotFoundException;

import br.com.senaijandira.kingsmoney.dao.CategoriaDAO;
import br.com.senaijandira.kingsmoney.model.Categoria;

public class AdicionarCategoriaActivity extends AppCompatActivity {
    private int COD_GALERIA = 1;
    private ImageView imgFoto;
    private EditText edtNome;
    private Bitmap foto;
    private Categoria categoriaAtual;
    private boolean onTransition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_categoria);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imgFoto = (ImageView) findViewById(R.id.img_add_categoria_foto);
        edtNome = (EditText) findViewById(R.id.edt_add_categoria_nome);

        int id = getIntent().getIntExtra("id", -1);
        if (id != -1) {
            toolbar.setTitle("Editar Categoria");
            categoriaAtual = CategoriaDAO.getInstance().selecionarUm(this, id);
            edtNome.setText(categoriaAtual.getNome());
            edtNome.setSelection(edtNome.getText().length());
            foto = categoriaAtual.getFoto();
            if (foto != null) {
                imgFoto.setImageBitmap(foto);
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_adicionar_categoria, menu);
        // Pinta o ícone de banco a partir do java, pois a partir do xml só é suportado em versões mais recentes do android.
        Drawable drawable = menu.findItem(R.id.item_salvar_categoria).getIcon();
        drawable.mutate();
        drawable.setColorFilter(ContextCompat.getColor(this, R.color.menuItem), PorterDuff.Mode.SRC_ATOP);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (onTransition) {
            return false;
        }

        if (item.getItemId() == R.id.item_salvar_categoria) {
            if (edtNome.getText().toString().equals("")) {
                edtNome.setError("Preencha esse campo.");
            } else {
                onTransition = true;
                Categoria categoria;
                if (categoriaAtual != null) {
                    categoria = categoriaAtual;
                } else {
                    categoria = new Categoria();
                }

                categoria.setNome(edtNome.getText().toString());
                categoria.setFoto(foto);
                if (categoriaAtual != null) {
                    CategoriaDAO.getInstance().atualizar(this, categoria);
                } else {
                    CategoriaDAO.getInstance().inserir(this, categoria);
                }

                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void abrirGaleria(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, COD_GALERIA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == COD_GALERIA && resultCode == Activity.RESULT_OK) {
            try {
                foto = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData()));
                if (foto.getHeight() > 512) {
                    // Gera uma imagem com novo tamanho caso a imagem seja muito grande, evitando erros no aplicativo.
                    int newHeight = (int) (foto.getHeight() * (512.0 / foto.getWidth()));
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(foto, 512, newHeight, true);
                    imgFoto.setImageBitmap(scaledBitmap);
                } else {
                    imgFoto.setImageBitmap(foto);
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}