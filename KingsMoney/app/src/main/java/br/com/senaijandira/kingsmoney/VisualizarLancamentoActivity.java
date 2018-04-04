package br.com.senaijandira.kingsmoney;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import br.com.senaijandira.kingsmoney.dao.CategoriaDAO;
import br.com.senaijandira.kingsmoney.dao.LancamentoDAO;
import br.com.senaijandira.kingsmoney.model.Categoria;
import br.com.senaijandira.kingsmoney.model.Lancamento;
import br.com.senaijandira.kingsmoney.util.MoneyFormatter;

public class VisualizarLancamentoActivity extends AppCompatActivity {
    private TextView txtTitulo, txtValor, txtDescricao, txtData, txtCategoria;
    private Lancamento lancamento;
    private int idLancamento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_lancamento);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AppBarLayout appBar = (AppBarLayout) findViewById(R.id.app_bar_layout);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        idLancamento = getIntent().getIntExtra("id", 0);
        lancamento = LancamentoDAO.getInstance().selecionarUm(this, idLancamento);
        Categoria categoria = CategoriaDAO.getInstance().selecionarUm(this, lancamento.getIdCategoria());
        if (lancamento.getTipo() == 0) {
            getSupportActionBar().setTitle("Despesa");
            appBar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryNegative));
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDarkNegative));
        } else {
            getSupportActionBar().setTitle("Receita");
            appBar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }

        txtTitulo = (TextView) findViewById(R.id.txt_visualizar_lancamento_titulo);
        txtValor = (TextView) findViewById(R.id.txt_visualizar_lancamento_valor);
        txtDescricao = (TextView) findViewById(R.id.txt_visualizar_lancamento_descricao);
        txtData = (TextView) findViewById(R.id.txt_visualizar_lancamento_data);
        txtCategoria = (TextView) findViewById(R.id.txt_visualizar_lancamento_categoria);
    }

    @Override
    protected void onResume() {
        super.onResume();
        lancamento = LancamentoDAO.getInstance().selecionarUm(this, idLancamento);
        Categoria categoria = CategoriaDAO.getInstance().selecionarUm(this, lancamento.getIdCategoria());
        txtTitulo.setText(lancamento.getTitulo());
        txtValor.setText(MoneyFormatter.format(lancamento.getValor()));
        txtDescricao.setText(lancamento.getDescricao());
        txtData.setText(new SimpleDateFormat("dd/MM/yyyy").format(lancamento.getData()));
        txtCategoria.setText(categoria.getNome());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_visualizar_lancamento, menu);
        // Pinta o ícone de banco a partir do java, pois a partir do xml só é suportado em versões mais recentes do android.
        Drawable drawable = menu.findItem(R.id.item_editar_lancamento).getIcon();
        drawable.mutate();
        drawable.setColorFilter(ContextCompat.getColor(this, R.color.menuItem), PorterDuff.Mode.SRC_ATOP);
        Drawable drawable2 = menu.findItem(R.id.item_deletar_lancamento).getIcon();
        drawable2.mutate();
        drawable2.setColorFilter(ContextCompat.getColor(this, R.color.menuItem), PorterDuff.Mode.SRC_ATOP);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_editar_lancamento) {
            Intent intent = new Intent(this, AdicionarLancamentoActivity.class);
            intent.putExtra("tipo", lancamento.getTipo());
            intent.putExtra("id", lancamento.getId());
            startActivity(intent);
        } else if (item.getItemId() == R.id.item_deletar_lancamento) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Excluir");
            builder.setMessage("Tem certeza que deseja excluir esse item ?");
            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    LancamentoDAO.getInstance().remover(VisualizarLancamentoActivity.this, lancamento.getId());
                    finish();
                }
            });

            builder.setNegativeButton("Não", null);
            builder.create().show();
        }

        return super.onOptionsItemSelected(item);
    }
}