package br.com.portalautocenter.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.portalautocenter.app.R;
import br.com.portalautocenter.models.Pedido;


/**
 * Created by Jhoe on 06/06/2018.
 */

public class PedidoAdapter extends ArrayAdapter<Pedido> {
    public PedidoAdapter(Context context, ArrayList<Pedido> lstPedido){
        super(context, 0, lstPedido) ;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if (v == null){
            v = LayoutInflater.from(getContext()).inflate(R.layout.list_pedidos, null);
        }
        Pedido pedido = getItem(position);

        //Declaração dos objetos do Layout
        TextView txtPedido = (TextView)v.findViewById(R.id.txt_pedido);
        TextView txtProduto = (TextView)v.findViewById(R.id.txt_produto);
        TextView txtStatus = (TextView)v.findViewById(R.id.txt_status);

        txtPedido.setText("#"+pedido.getId());
        txtProduto.setText(pedido.getProduto());
        txtStatus.setText(pedido.getStats());
        return v;
    }
}
