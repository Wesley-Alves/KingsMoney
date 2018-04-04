package br.com.senaijandira.kingsmoney;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.senaijandira.kingsmoney.adapter.SelecionarCategoriaAdapter;
import br.com.senaijandira.kingsmoney.dao.CategoriaDAO;
import br.com.senaijandira.kingsmoney.dao.LancamentoDAO;
import br.com.senaijandira.kingsmoney.model.Categoria;
import br.com.senaijandira.kingsmoney.model.Lancamento;
import br.com.senaijandira.kingsmoney.util.MoneyFormatter;
import br.com.senaijandira.kingsmoney.util.MoneyTextWatcher;

public class AdicionarLancamentoActivity extends AppCompatActivity {
    private EditText edtValor, edtTitulo, edtDescricao, edtData, edtCategoria;
    private Calendar calendar = Calendar.getInstance();
    private MoneyTextWatcher moneyWatcher;
    private int tipoLancamento, idCategoria;
    private Date data;
    private Lancamento lancamentoAtual;
    private boolean onTransition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_lancamento);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        edtTitulo = (EditText) findViewById(R.id.edt_add_lancamento_titulo);
        edtDescricao = (EditText) findViewById(R.id.edt_add_lancamento_descricao);

        edtValor = (EditText) findViewById(R.id.edt_add_lancamento_valor);
        edtValor.setSelection(edtValor.getText().length());
        moneyWatcher = new MoneyTextWatcher(edtValor);
        edtValor.addTextChangedListener(moneyWatcher);

        edtData = (EditText) findViewById(R.id.edt_add_lancamento_data);
        edtData.setKeyListener(null);
        edtData.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (focus) {
                    selecionarData();
                }
            }
        });

        edtCategoria = (EditText) findViewById(R.id.edt_add_lancamento_categoria);
        edtCategoria.setKeyListener(null);
        edtCategoria.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (focus) {
                    selecionarCategoria();
                }
            }
        });

        tipoLancamento = getIntent().getIntExtra("tipo", 0);
        int idLancamento = getIntent().getIntExtra("id", -1);
        if (idLancamento != -1) {
            lancamentoAtual = LancamentoDAO.getInstance().selecionarUm(this, idLancamento);
            Categoria categoria = CategoriaDAO.getInstance().selecionarUm(this, lancamentoAtual.getIdCategoria());
            edtValor.setText(MoneyFormatter.format(lancamentoAtual.getValor()));
            edtValor.setSelection(edtValor.getText().length());
            edtTitulo.setText(lancamentoAtual.getTitulo());
            edtDescricao.setText(lancamentoAtual.getDescricao());
            data = lancamentoAtual.getData();
            calendar.setTime(data);
            edtData.setText(new SimpleDateFormat("dd/MM/yyyy").format(lancamentoAtual.getData()));
            idCategoria = categoria.getId();
            edtCategoria.setText(categoria.getNome());
        }

        if (tipoLancamento == 0) {
            if (idLancamento == -1) {
                getSupportActionBar().setTitle("Adicionar Despesa");
            } else {
                getSupportActionBar().setTitle("Editar Despesa");
            }

            toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryNegative));
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDarkNegative));
        } else {
            if (idLancamento == -1) {
                getSupportActionBar().setTitle("Adicionar Receita");
            } else {
                getSupportActionBar().setTitle("Editar Receita");
            }

            toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_adicionar_lancamento, menu);
        // Pinta o ícone de banco a partir do java, pois a partir do xml só é suportado em versões mais recentes do android.
        Drawable drawable = menu.findItem(R.id.item_salvar_lancamento).getIcon();
        drawable.mutate();
        drawable.setColorFilter(ContextCompat.getColor(this, R.color.menuItem), PorterDuff.Mode.SRC_ATOP);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (onTransition) {
            return false;
        }

        if (item.getItemId() == R.id.item_salvar_lancamento) {
            if (edtValor.getText().toString().equals("")) {
                edtValor.setError("Preencha esse campo.");
            } else if (edtTitulo.getText().toString().equals("")) {
                edtTitulo.setError("Preencha esse campo.");
            } else if (edtData.getText().toString().equals("")) {
                edtData.setError("Preencha esse campo.");
            } else if (edtCategoria.getText().toString().equals("")) {
                edtCategoria.setError("Preencha esse campo.");
            } else {
                onTransition = true;
                Lancamento lancamento;
                if (lancamentoAtual != null) {
                    lancamento = lancamentoAtual;
                } else {
                    lancamento = new Lancamento();
                }

                lancamento.setValor(moneyWatcher.getValue());
                lancamento.setTitulo(edtTitulo.getText().toString());
                lancamento.setDescricao(edtDescricao.getText().toString());
                lancamento.setTipo(tipoLancamento);
                lancamento.setData(data);
                lancamento.setIdCategoria(idCategoria);
                if (lancamentoAtual != null) {
                    LancamentoDAO.getInstance().atualizar(this, lancamento);
                } else {
                    LancamentoDAO.getInstance().inserir(this, lancamento);
                }

                finish();
            }

        } else if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void selecionarData() {
        // Motra um dialogo de calendário para a seleção de uma data.
        DatePickerDialog dialog = new DatePickerDialog(this,
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, day);
                    data = calendar.getTime();
                    edtData.setText(new SimpleDateFormat("dd/MM/yyyy").format(data));
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    public void selecionarDataClick(View view) {
        if (edtData.hasFocus()) {
            selecionarData();
        }
    }

    public void selecionarCategoria() {
        // Mostra um dialogo com a lista de categorias para a seleção.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Categorias");
        final SelecionarCategoriaAdapter adapter = new SelecionarCategoriaAdapter(this, CategoriaDAO.getInstance().selecionarTodos(this));
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Categoria categoria = adapter.getItem(item);
                edtCategoria.setText(categoria.getNome());
                idCategoria = categoria.getId();
            }
        });

        builder.setNegativeButton("Cancelar", null);
        AlertDialog alertDialogObject = builder.create();
        ListView listView = alertDialogObject.getListView();
        listView.setDivider(new ColorDrawable(Color.LTGRAY));
        listView.setDividerHeight(2);
        alertDialogObject.show();
    }

    public void selecionarCategoriaClick(View view) {
        if (edtCategoria.hasFocus()) {
            selecionarCategoria();
        }
    }
}