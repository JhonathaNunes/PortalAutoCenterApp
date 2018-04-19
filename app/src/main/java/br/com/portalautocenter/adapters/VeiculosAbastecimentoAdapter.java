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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import br.com.portalautocenter.app.R;
import br.com.portalautocenter.models.Veiculo;

/**
 * Created by 16254855 on 18/04/2018.
 */

public class VeiculosAbastecimentoAdapter extends ArrayAdapter<Veiculo> {
    public VeiculosAbastecimentoAdapter(Context context, ArrayList<Veiculo> lstVeiculo){
        super(context, 0, lstVeiculo);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if (v == null){
            v = LayoutInflater.from(getContext()).inflate(R.layout.list_veiculos, null);
        }
        //Pega o produto de determinada posição na lista
        Veiculo veiculo = getItem(position);

        //Declaração dos objetos do Layout
        TextView txtModelo = (TextView)v.findViewById(R.id.txt_modelo);
        TextView txtPlaca = (TextView)v.findViewById(R.id.txt_placa);
        ImageView imgCarro = (ImageView)v.findViewById(R.id.car_img);



        //Colocando a imagem como Picasso
        String url = "http://10.0.2.2/inf4m/PortalAutoCenter/TCCPortalAutoCenter/"+ veiculo.getFoto();
        Picasso.with(getContext()).load(url).into(imgCarro);

        txtModelo.setText(veiculo.getModelo());
        txtPlaca.setText(veiculo.getPlaca());

        return v;
    }
}
