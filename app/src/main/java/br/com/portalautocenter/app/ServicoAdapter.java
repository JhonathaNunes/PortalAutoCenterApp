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

import java.util.ArrayList;


/**
 * Created by 16254855 on 06/04/2018.
 */

public class ServicoAdapter extends ArrayAdapter<Servico>{
    public ServicoAdapter(Context context, ArrayList<Servico> lstServico){
        super(context, 0, lstServico);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if (v == null){
            v = LayoutInflater.from(getContext()).inflate(R.layout.list_servicos, null);
        }
        //Pega o produto de determinada posição na lista
        Servico servico = getItem(position);

        //Declaração dos objetos do Layout
        TextView txtServico = (TextView)v.findViewById(R.id.txt_servico);
        TextView txtDesc = (TextView)v.findViewById(R.id.txt_desc);

        ImageView imgServico = (ImageView)v.findViewById(R.id.imagem_servico);

        //Colocando a imagem como Picasso
        String url = "http://10.107.134.16/INF4/TCC-Portal-auto-center3/PortalAutoCenter2/TCCPortalAutoCenter/cms/"+servico.getImagem();
        Picasso.with(getContext()).load(url).into(imgServico);

        txtServico.setText(servico.getNome());
        txtDesc.setText(servico.getDescricao());

        return v;
    }
}
