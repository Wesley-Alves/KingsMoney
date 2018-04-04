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
import br.com.senaijandira.kingsmoney.model.Categoria;

public class SelecionarCategoriaAdapter extends ArrayAdapter<Categoria> {
    public SelecionarCategoriaAdapter(Context ctx, ArrayList<Categoria> categorias) {
        super(ctx, 0, categorias);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_selecionar_categoria_item, null);
        }

        Categoria categoria = getItem(position);
        ImageView imgFoto = (ImageView) view.findViewById(R.id.img_item_selecionar_categoria_foto);
        TextView txtNome = (TextView) view.findViewById(R.id.txt_item_selecionar_categoria_nome);

        txtNome.setText(categoria.getNome());
        if (categoria.getFoto() != null) {
            imgFoto.setImageBitmap(categoria.getFoto());
        } else {
            imgFoto.setImageResource(android.R.drawable.ic_menu_camera);
        }

        return view;
    }
}