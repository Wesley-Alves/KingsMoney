package br.com.senaijandira.kingsmoney;

import android.animation.Animator;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import br.com.senaijandira.kingsmoney.adapter.LancamentoAdapter;
import br.com.senaijandira.kingsmoney.dao.LancamentoDAO;
import br.com.senaijandira.kingsmoney.model.Lancamento;

public class LancamentosActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FloatingActionButton fabAddLancamento, fabAddReceita, fabAddDespesa;
    private LinearLayout fabAddReceitaLayout, fabAddDespesaLayout;
    private View fabBackground;
    private boolean isFabOpen;;
    private int oldFabScrollPosition;
    private Calendar calendar = Calendar.getInstance();
    private LancamentoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lancamentos);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setToolbarTitle();

        // Cria um menu flutuante quando clica no botão de acicionar lançamento.
        fabAddReceitaLayout = (LinearLayout) findViewById(R.id.fab_add_receita_layout);
        fabAddDespesaLayout = (LinearLayout) findViewById(R.id.fab_add_despesa_layout);
        fabAddLancamento = (FloatingActionButton) findViewById(R.id.fab_add_lancamento);
        fabAddReceita = (FloatingActionButton) findViewById(R.id.fab_add_receita);
        fabAddDespesa = (FloatingActionButton) findViewById(R.id.fab_add_despesa);
        fabBackground = findViewById(R.id.fab_background);

        fabBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFabMenu();
            }
        });

        fabAddLancamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFabOpen) {
                    showFabMenu();
                } else {
                    closeFabMenu();
                }
            }
        });

        fabAddDespesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LancamentosActivity.this, AdicionarLancamentoActivity.class);
                intent.putExtra("tipo", 0);
                startActivity(intent);
                closeFabMenu();
            }
        });

        fabAddReceita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LancamentosActivity.this, AdicionarLancamentoActivity.class);
                intent.putExtra("tipo", 1);
                startActivity(intent);
                closeFabMenu();
            }
        });

        ListView listViewLancamentos = (ListView) findViewById(R.id.list_view_lancamentos);
        adapter = new LancamentoAdapter(this, new ArrayList<Lancamento>());
        listViewLancamentos.setAdapter(adapter);
        listViewLancamentos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(LancamentosActivity.this, VisualizarLancamentoActivity.class);
                intent.putExtra("id", adapter.getItem(i).getId());
                startActivity(intent);
            }
        });

        listViewLancamentos.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // Sumir ou aparecer o botão flutuante enquanto da scroll na ListView.
                if (firstVisibleItem > oldFabScrollPosition) {
                    fabAddLancamento.animate().cancel();
                    fabAddLancamento.animate().translationY(150);
                } else if (firstVisibleItem < oldFabScrollPosition || firstVisibleItem <= 0) {
                    fabAddLancamento.animate().cancel();
                    fabAddLancamento.animate().translationY(0);
                }

                oldFabScrollPosition = firstVisibleItem;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateList();
    }

    private void updateList() {
        ArrayList<Lancamento> lancamentos = filtrarLancamentos(LancamentoDAO.getInstance().selecionarTodos(this));
        adapter.clear();
        adapter.addAll(lancamentos);
    }

    private ArrayList<Lancamento> filtrarLancamentos(ArrayList<Lancamento> lancamentos) {
        // Compara o mês e o ano dos lançamentos para a data selecionada.
        ArrayList<Lancamento> lancamentosMes = new ArrayList<>();
        Calendar calendar2 = Calendar.getInstance();
        for (Lancamento lancamento : lancamentos) {
            calendar2.setTime(lancamento.getData());
            if (calendar.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH) && calendar.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)) {
                lancamentosMes.add(lancamento);
            }
        }

        return lancamentosMes;
    }

    private void showFabMenu(){
        isFabOpen = true;
        fabAddReceitaLayout.setVisibility(View.VISIBLE);
        fabAddDespesaLayout.setVisibility(View.VISIBLE);
        fabBackground.setVisibility(View.VISIBLE);
        fabAddLancamento.animate().rotationBy(180);
        fabAddDespesaLayout.animate().translationY(-getResources().getDimension(R.dimen.fab_despesa_y));
        fabAddReceitaLayout.animate().translationY(-getResources().getDimension(R.dimen.fab_receita_y));
    }

    private void closeFabMenu(){
        isFabOpen = false;
        fabBackground.setVisibility(View.GONE);
        fabAddLancamento.animate().rotationBy(-180);
        fabAddDespesaLayout.animate().translationY(0);
        fabAddReceitaLayout.animate().translationY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (!isFabOpen){
                    fabAddReceitaLayout.setVisibility(View.GONE);
                    fabAddDespesaLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(!isFabOpen){
            super.onBackPressed();
        } else {
            closeFabMenu();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lancamentos, menu);
        // Pinta o ícone de banco a partir do java, pois a partir do xml só é suportado em versões mais recentes do android.
        Drawable drawable = menu.findItem(R.id.item_selecionar_mes).getIcon();
        drawable.mutate();
        drawable.setColorFilter(ContextCompat.getColor(this, R.color.menuItem), PorterDuff.Mode.SRC_ATOP);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Dialogo para selecionar um mês e um ano para filtrar os lançamentos por data.
        if (item.getItemId() == R.id.item_selecionar_mes) {
            DatePickerDialog dialog = new DatePickerDialog(this, R.style.DatePickerDialog, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, day);
                    setToolbarTitle();
                    updateList();
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            dialog.getDatePicker().findViewById(getResources().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
            dialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setToolbarTitle() {
        int year = calendar.get(Calendar.YEAR);
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
        toolbar.setTitle(month.toUpperCase() + " / " + year);
    }
}