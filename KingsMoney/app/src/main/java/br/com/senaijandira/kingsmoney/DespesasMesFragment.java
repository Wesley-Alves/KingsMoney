package br.com.senaijandira.kingsmoney;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import br.com.senaijandira.kingsmoney.adapter.EstatisticaMesAdapter;
import br.com.senaijandira.kingsmoney.dao.LancamentoDAO;
import br.com.senaijandira.kingsmoney.model.EstatisticaMes;
import br.com.senaijandira.kingsmoney.model.Lancamento;

public class DespesasMesFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_despesas_mes, container, false);
        ListView listView = (ListView) view.findViewById(R.id.list_view_estatisticas_mes);

        // Separa todas as despesas cadastradas com mês igual e calcula o valor total.
        double saldo = LancamentoDAO.getInstance().getSaldoGeral(getContext())[0];
        ArrayList<EstatisticaMes> estatisticas = new ArrayList<>();
        ArrayList<Lancamento> lancamentos = LancamentoDAO.getInstance().selecionarTodos(getContext());

        lancamentosLoop:
        for (Lancamento lancamento : lancamentos) {
            if (lancamento.getTipo() == 0) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(lancamento.getData());

                for (EstatisticaMes estatistica : estatisticas) {
                    if (calendar.get(Calendar.MONTH) == estatistica.getCalendar().get(Calendar.MONTH) && calendar.get(Calendar.YEAR) == estatistica.getCalendar().get(Calendar.YEAR)) {
                        estatistica.setValor(estatistica.getValor() + lancamento.getValor());
                        estatistica.setPorcentagem(estatistica.getValor() * 100 / saldo);
                        continue lancamentosLoop; // Continua para o próximo item do loop de lançamentos
                    }
                }

                EstatisticaMes estatistica = new EstatisticaMes();
                estatistica.setData(lancamento.getData());
                estatistica.setValor(lancamento.getValor());
                estatistica.setPorcentagem(lancamento.getValor() * 100 / saldo);
                estatistica.setCalendar(calendar);
                estatisticas.add(estatistica);
            }
        }

        // Ordena a lista por data
        Collections.sort(estatisticas, new Comparator<EstatisticaMes>() {
            @Override
            public int compare(EstatisticaMes e1, EstatisticaMes e2) {
                return e2.getData().compareTo(e1.getData());
            }
        });

        EstatisticaMesAdapter adapter = new EstatisticaMesAdapter(getContext(), estatisticas);
        listView.setAdapter(adapter);
        return view;
    }
}