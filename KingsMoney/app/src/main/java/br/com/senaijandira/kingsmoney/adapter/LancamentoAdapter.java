package br.com.senaijandira.kingsmoney.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.senaijandira.kingsmoney.R;
import br.com.senaijandira.kingsmoney.dao.CategoriaDAO;
import br.com.senaijandira.kingsmoney.model.Categoria;
import br.com.senaijandira.kingsmoney.model.Lancamento;
import br.com.senaijandira.kingsmoney.util.MoneyFormatter;

public class LancamentoAdapter extends ArrayAdapter<Lancamento> {
    public LancamentoAdapter(Context ctx, ArrayList<Lancamento> lancamentos) {
        super(ctx, 0, lancamentos);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_lancamentos_item, null);
        }

        Lancamento lancamento = getItem(position);
        Categoria categoria = CategoriaDAO.getInstance().selecionarUm(getContext(), lancamento.getIdCategoria());
        ImageView imgFoto = (ImageView) view.findViewById(R.id.img_item_lancamento_foto);
        TextView txtNome = (TextView) view.findViewById(R.id.txt_item_lancamento_nome);
        TextView txtCategoria = (TextView) view.findViewById(R.id.txt_item_lancamento_categoria);
        TextView txtValor = (TextView) view.findViewById(R.id.txt_item_lancamento_valor);

        txtNome.setText(lancamento.getTitulo());
        txtCategoria.setText(categoria.getNome());
        if (lancamento.getTipo() == 0) {
            txtValor.setText(MoneyFormatter.format(-lancamento.getValor()));
            txtValor.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDarkNegative));
        } else {
            txtValor.setText(MoneyFormatter.format(lancamento.getValor()));
            txtValor.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        }

        if (categoria.getFoto() != null) {
            imgFoto.setImageBitmap(categoria.getFoto());
        } else {
            imgFoto.setImageResource(android.R.drawable.ic_menu_camera);
        }

        return view;
    }
}