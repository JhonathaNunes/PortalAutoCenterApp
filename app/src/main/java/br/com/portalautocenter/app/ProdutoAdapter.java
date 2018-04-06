package br.com.portalautocenter.app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by 16254855 on 04/04/2018.
 */

public class ProdutoAdapter extends ArrayAdapter<Produto> {
    public ProdutoAdapter(Context context, ArrayList<Produto> lstProdutos){
        super(context, 0, lstProdutos);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if (v == null){
            v = LayoutInflater.from(getContext()).inflate(R.layout.list_produtos, null);
        }
        //Pega o produto de determinada posição na lista
        Produto produto = getItem(position);

        //Declaração dos objetos do Layout
        TextView txtProduto = (TextView)v.findViewById(R.id.txt_produto);
        TextView txtDesc = (TextView)v.findViewById(R.id.txt_desc);
        TextView txtPreco = (TextView)v.findViewById(R.id.txt_preco);

        double precoProduto = produto.getPreco();

        //Converte para real
        Locale brasil = new Locale("pt", "BR");
        NumberFormat nf = NumberFormat.getCurrencyInstance(brasil);

        String preco = nf.format(precoProduto);

        ImageView imgProduto = (ImageView)v.findViewById(R.id.imagem_produto);

        //Colocando a imagem como Picasso
        String url = "http://10.0.2.2/inf4m/PortalAutoCenter/TCCPortalAutoCenter/cms/"+produto.getImagem();
        Picasso.with(getContext()).load(url).into(imgProduto);

        txtProduto.setText(produto.getNome());
        txtDesc.setText(produto.getDescricao());
        txtPreco.setText(preco);

        return v;
    }
}
