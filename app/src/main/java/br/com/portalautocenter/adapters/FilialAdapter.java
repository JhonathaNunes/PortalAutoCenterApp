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
import br.com.portalautocenter.models.Endereco;
import br.com.portalautocenter.models.Filial;

/**
 * Created by Jhoe on 06/06/2018.
 */

public class FilialAdapter extends ArrayAdapter<Filial> {
    public FilialAdapter(Context context, ArrayList<Filial> lstFilial){
        super(context, 0, lstFilial) ;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if (v == null){
            v = LayoutInflater.from(getContext()).inflate(R.layout.list_filial, null);
        }
        Filial filial = getItem(position);

        //Declaração dos objetos do Layout
        TextView txtNome = (TextView)v.findViewById(R.id.txt_nome);
        TextView txtEndereco = (TextView)v.findViewById(R.id.txt_endereco);

        txtNome.setText(filial.getNome());
        txtEndereco.setText(filial.getNome());
        return v;
    }
}
