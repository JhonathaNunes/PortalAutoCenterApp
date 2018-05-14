package br.com.portalautocenter.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import br.com.portalautocenter.app.R;
import br.com.portalautocenter.models.Veiculo;

/**
 * Created by 16254855 on 26/04/2018.
 */

public class VeiculosAdapter extends PagerAdapter {

    private Context aContext;
    private LayoutInflater aLayoutInflater;
    private ArrayList<Veiculo> aResources;

    public VeiculosAdapter(Context context, ArrayList<Veiculo> resources){
        aContext = context;
        aResources = resources;
        aLayoutInflater = (LayoutInflater) aContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return aResources.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View prestView = aLayoutInflater.inflate(R.layout.list_veiculos_usuario, container, false);

        Veiculo v = aResources.get(position);

        ImageView imageView = (ImageView) prestView.findViewById(R.id.img_veiculo);
        TextView txtPlaca = (TextView) prestView.findViewById(R.id.txt_placa);
        TextView txtModelo = (TextView) prestView.findViewById(R.id.txt_modelo);
        TextView txtMarca = (TextView) prestView.findViewById(R.id.txt_marca);

        txtMarca.setText(v.getMarca());
        txtModelo.setText(v.getModelo());
        txtPlaca.setText(v.getPlaca());

        String url = "http://10.107.144.17/inf4m/PortalAutoCenter/TCCPortalAutoCenter/"+v.getFoto();
        Picasso.with(aContext).load(url).into(imageView);

        container.addView(prestView);

        return  prestView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
