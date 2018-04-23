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
import br.com.portalautocenter.models.Abastecimento;
import br.com.portalautocenter.utils.ConvetBrasilKt;

/**
 * Created by 16254855 on 23/04/2018.
 */

public class AbastecimentoAdapter extends ArrayAdapter<Abastecimento> {
    public AbastecimentoAdapter(Context context, ArrayList<Abastecimento> list_abastecimento){
        super(context, 0, list_abastecimento);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if (v == null){
            v = LayoutInflater.from(getContext()).inflate(R.layout.list_abastecimentos, null);
        }

        Abastecimento a = getItem(position);

        TextView txt_data = (TextView) v.findViewById(R.id.txt_data);
        TextView txt_posto = (TextView) v.findViewById(R.id.txt_posto);
        TextView txt_valor = (TextView) v.findViewById(R.id.txt_valor);
        TextView txt_abastecido = (TextView) v.findViewById(R.id.txt_abastecido);

        String preco = ConvetBrasilKt.converteParaReal(a.getPreco());
        String abastecido = ConvetBrasilKt.converteParaReal(a.getLitros());

        txt_data.setText(a.getData());
        txt_posto.setText(a.getPosto());
        txt_abastecido.setText(abastecido);
        txt_valor.setText(preco);

        return v;
    }
}
