package br.com.portalautocenter.adapters;

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

import br.com.portalautocenter.app.R;
import br.com.portalautocenter.models.Endereco;

/**
 * Created by 16254855 on 15/05/2018.
 */

public class EnderecoAdapter extends ArrayAdapter<Endereco> {
    public EnderecoAdapter(Context context, ArrayList<Endereco> lstEnderecos){
        super(context, 0, lstEnderecos) ; 
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if (v == null){
            v = LayoutInflater.from(getContext()).inflate(R.layout.list_enderecos, null);
        }
        //Pega o Endereco de determinada posição na lista
        Endereco endereco = getItem(position);

        //Declaração dos objetos do Layout
        TextView txtLogradouro = (TextView)v.findViewById(R.id.txt_logradouro);
        TextView txtNumero = (TextView)v.findViewById(R.id.txt_numero);
        TextView txtBarro = (TextView)v.findViewById(R.id.txt_bairro);
        TextView txtCep =(TextView)v.findViewById(R.id.txt_cep);
        TextView txtTpEnd =(TextView)v.findViewById(R.id.txt_tpEndereco);

        txtLogradouro.setText(endereco.getLogradouro());
        txtNumero.setText(endereco.getNumero());
        txtBarro.setText(endereco.getBairro());
        txtCep.setText(endereco.getCep());
        txtTpEnd.setText(endereco.getDescricao());


        return v;
    }
}
