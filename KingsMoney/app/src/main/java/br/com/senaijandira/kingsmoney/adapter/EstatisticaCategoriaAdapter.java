package br.com.senaijandira.kingsmoney.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.senaijandira.kingsmoney.R;
import br.com.senaijandira.kingsmoney.model.EstatisticaCategoria;
import br.com.senaijandira.kingsmoney.util.MoneyFormatter;

public class EstatisticaCategoriaAdapter extends ArrayAdapter<EstatisticaCategoria> {
    public EstatisticaCategoriaAdapter(Context ctx, ArrayList<EstatisticaCategoria> estatisticas) {
        super(ctx, 0, estatisticas);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_estatisticas_categoria_item, null);
        }

        EstatisticaCategoria estatistica = getItem(position);
        ImageView imgFoto = (ImageView) view.findViewById(R.id.img_item_estatistica_categoria_foto);
        TextView txtNome = (TextView) view.findViewById(R.id.txt_item_estatistica_categoria_nome);
        TextView txtPorcentagem = (TextView) view.findViewById(R.id.txt_item_estatistica_categoria_porcentagem);
        TextView txtValor = (TextView) view.findViewById(R.id.txt_item_estatistica_categoria_valor);

        txtNome.setText(estatistica.getNome());
        txtPorcentagem.setText(String.format("%.2f", estatistica.getPorcentagem()) + "%");
        txtValor.setText(MoneyFormatter.format(estatistica.getValor()));
        if (estatistica.getFoto() != null) {
            imgFoto.setImageBitmap(estatistica.getFoto());
        } else {
            imgFoto.setImageResource(android.R.drawable.ic_menu_camera);
        }

        return view;
    }
}