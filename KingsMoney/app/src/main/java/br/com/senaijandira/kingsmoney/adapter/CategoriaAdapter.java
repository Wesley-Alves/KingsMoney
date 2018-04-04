package br.com.senaijandira.kingsmoney.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.senaijandira.kingsmoney.AdicionarCategoriaActivity;
import br.com.senaijandira.kingsmoney.CategoriasActivity;
import br.com.senaijandira.kingsmoney.R;
import br.com.senaijandira.kingsmoney.dao.CategoriaDAO;
import br.com.senaijandira.kingsmoney.dao.LancamentoDAO;
import br.com.senaijandira.kingsmoney.model.Categoria;

public class CategoriaAdapter extends ArrayAdapter<Categoria> {
    public CategoriaAdapter(Context ctx, ArrayList<Categoria> categorias) {
        super(ctx, 0, categorias);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_categorias_item, null);
        }

        final Categoria categoria = getItem(position);
        ImageView imgFoto = (ImageView) view.findViewById(R.id.img_item_categoria_foto);
        TextView txtNome = (TextView) view.findViewById(R.id.txt_item_categoria_nome);
        ImageView imgEditar = (ImageView) view.findViewById(R.id.img_editar_categoria);
        ImageView imgDeletar = (ImageView) view.findViewById(R.id.img_deletar_categoria);

        txtNome.setText(categoria.getNome());
        if (categoria.getFoto() != null) {
            imgFoto.setImageBitmap(categoria.getFoto());
        } else {
            imgFoto.setImageResource(android.R.drawable.ic_menu_camera);
        }

        imgEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AdicionarCategoriaActivity.class);
                intent.putExtra("id", categoria.getId());
                ((CategoriasActivity) getContext()).startActivity(intent);
            }
        });

        imgDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Excluir");
                if (LancamentoDAO.getInstance().checarLancamentosByCategoria(getContext(), categoria.getId())) {
                    builder.setMessage("Não é possível excluir esta categoria, pois ela já possui lançamentos cadastrados.");
                    builder.setNegativeButton("Ok", null);

                    AlertDialog dialog = builder.create();
                    dialog.show();
                    TextView messageView = (TextView) dialog.findViewById(android.R.id.message);
                    messageView.setGravity(Gravity.CENTER);
                } else {
                    builder.setMessage("Tem certeza que deseja excluir esse item ?");
                    builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            CategoriaDAO.getInstance().remover(getContext(), categoria.getId());
                            ((CategoriasActivity) getContext()).updateList();
                        }
                    });

                    builder.setNegativeButton("Não", null);
                    builder.create().show();
                }
            }
        });

        return view;
    }
}