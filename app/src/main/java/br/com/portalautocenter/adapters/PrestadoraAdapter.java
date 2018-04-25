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
import br.com.portalautocenter.models.Prestadora;

/**
 * Created by 16254855 on 24/04/2018.
 */

public class PrestadoraAdapter extends PagerAdapter{

    private Context aContext;
    private LayoutInflater aLayoutInflater;
    private ArrayList<Prestadora> aResources;

    public PrestadoraAdapter(Context context, ArrayList<Prestadora> resources){
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
        View prestView = aLayoutInflater.inflate(R.layout.list_prestadora, container, false);

        Prestadora p = aResources.get(position);

        ImageView imageView = (ImageView) prestView.findViewById(R.id.img_prestadora);
        TextView txtDesc = (TextView) prestView.findViewById(R.id.txt_descPrestadora);
        TextView txtNome = (TextView) prestView.findViewById(R.id.txt_nomeFantasia);

        txtDesc.setText(p.getDescricao());
        txtNome.setText(p.getNomeFantasia());
        String url = "http://10.0.2.2/inf4m/PortalAutoCenter/TCCPortalAutoCenter/"+p.getFotoPrestadora();
        Picasso.with(aContext).load(url).into(imageView);
        //imageView.setImageResource(aResources[position]);


        container.addView(prestView);

        return  prestView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
