package br.com.senaijandira.kingsmoney.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import br.com.senaijandira.kingsmoney.R;
import br.com.senaijandira.kingsmoney.model.EstatisticaMes;
import br.com.senaijandira.kingsmoney.util.MoneyFormatter;

public class EstatisticaMesAdapter  extends ArrayAdapter<EstatisticaMes> {
    public EstatisticaMesAdapter(Context ctx, ArrayList<EstatisticaMes> estatisticas) {
        super(ctx, 0, estatisticas);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_estatisticas_mes_item, null);
        }

        EstatisticaMes estatistica = getItem(position);
        TextView txtNome = (TextView) view.findViewById(R.id.txt_item_estatistica_mes_nome);
        TextView txtPorcentagem = (TextView) view.findViewById(R.id.txt_item_estatistica_mes_porcentagem);
        TextView txtValor = (TextView) view.findViewById(R.id.txt_item_estatistica_mes_valor);

        String nome = new SimpleDateFormat("MMMM - yyyy", new Locale("pt", "BR")).format(estatistica.getData());
        txtNome.setText(nome.substring(0, 1).toUpperCase() + nome.substring(1));
        txtPorcentagem.setText(String.format("%.2f", estatistica.getPorcentagem()) + "%");
        txtValor.setText(MoneyFormatter.format(estatistica.getValor()));
        return view;
    }
}