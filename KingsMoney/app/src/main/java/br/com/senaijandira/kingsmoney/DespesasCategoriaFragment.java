package br.com.senaijandira.kingsmoney;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import br.com.senaijandira.kingsmoney.adapter.EstatisticaCategoriaAdapter;
import br.com.senaijandira.kingsmoney.dao.CategoriaDAO;
import br.com.senaijandira.kingsmoney.dao.LancamentoDAO;
import br.com.senaijandira.kingsmoney.model.Categoria;
import br.com.senaijandira.kingsmoney.model.EstatisticaCategoria;

public class DespesasCategoriaFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_despesas_categoria, container, false);
        ListView listView = (ListView) view.findViewById(R.id.list_view_estatisticas_categorias);

        // Pega o total de despesas de cada categoria e calcula a porcentagem a partir do valor total.
        double saldo = LancamentoDAO.getInstance().getSaldoGeral(getContext())[0];
        ArrayList<EstatisticaCategoria> estatisticas = new ArrayList<>();
        ArrayList<Categoria> categorias = CategoriaDAO.getInstance().selecionarTodos(getContext());
        for (Categoria categoria : categorias) {
            EstatisticaCategoria estatistica = new EstatisticaCategoria();
            estatistica.setNome(categoria.getNome());
            estatistica.setFoto(categoria.getFoto());
            double valor = LancamentoDAO.getInstance().getValorByCategoria(getContext(), categoria.getId());
            estatistica.setValor(valor);
            estatistica.setPorcentagem(valor * 100 / saldo);
            estatisticas.add(estatistica);
        }

        EstatisticaCategoriaAdapter adapter = new EstatisticaCategoriaAdapter(getContext(), estatisticas);
        listView.setAdapter(adapter);
        return view;
    }
}