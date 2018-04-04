package br.com.senaijandira.kingsmoney;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;

import br.com.senaijandira.kingsmoney.adapter.CategoriaAdapter;
import br.com.senaijandira.kingsmoney.dao.CategoriaDAO;
import br.com.senaijandira.kingsmoney.model.Categoria;

public class CategoriasActivity extends AppCompatActivity {
    private CategoriaAdapter adapter;
    private int oldFabScrollPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final FloatingActionButton fabAddCategoria = (FloatingActionButton) findViewById(R.id.fab_add_categoria);
        ListView listViewCategorias = (ListView) findViewById(R.id.list_view_categorias);
        adapter = new CategoriaAdapter(this, new ArrayList<Categoria>());
        listViewCategorias.setAdapter(adapter);
        listViewCategorias.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // Sumir ou aparecer o botão flutuante enquanto da scroll na ListView.
                if (firstVisibleItem > oldFabScrollPosition) {
                    fabAddCategoria.animate().cancel();
                    fabAddCategoria.animate().translationY(150);
                } else if (firstVisibleItem < oldFabScrollPosition || firstVisibleItem <= 0) {
                    fabAddCategoria.animate().cancel();
                    fabAddCategoria.animate().translationY(0);
                }

                oldFabScrollPosition = firstVisibleItem;
            }
        });
    }

    @Override
    protected void onResume() {
        updateList();
        super.onResume();
    }

    public void updateList() {
        ArrayList<Categoria> categorias = CategoriaDAO.getInstance().selecionarTodos(this);
        adapter.clear();
        adapter.addAll(categorias);
    }

    public void adicionarCategoria(View view) {
        startActivity(new Intent(getApplicationContext(), AdicionarCategoriaActivity.class));
    }
}