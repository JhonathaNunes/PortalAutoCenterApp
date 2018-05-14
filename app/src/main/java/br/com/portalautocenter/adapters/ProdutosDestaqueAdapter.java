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
import br.com.portalautocenter.models.Produto;
import br.com.portalautocenter.utils.ConvetBrasilKt;

/**
 * Created by 16254855 on 25/04/2018.
 */

public class ProdutosDestaqueAdapter extends PagerAdapter {
    private Context aContext;
    private LayoutInflater aLayoutInflater;
    private ArrayList<Produto> aResources;

    public ProdutosDestaqueAdapter(Context context, ArrayList<Produto> resources){
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
        View prestView = aLayoutInflater.inflate(R.layout.list_produtos_destaque, container, false);

        Produto p = aResources.get(position);

        ImageView imageView = (ImageView) prestView.findViewById(R.id.imagem_produto);
        TextView txtDesc = (TextView) prestView.findViewById(R.id.txt_desc);
        TextView txtNome = (TextView) prestView.findViewById(R.id.txt_produto);
        TextView txtPreco = (TextView)prestView.findViewById(R.id.txt_preco);

        double precoProduto = p.getPreco();

        String preco = ConvetBrasilKt.converteParaReal(precoProduto);

        txtDesc.setText(p.getDescricao());
        txtNome.setText(p.getNome());
        txtPreco.setText(preco);
        String url = "http://10.107.144.17/inf4m/PortalAutoCenter/TCCPortalAutoCenter/cms/"+p.getImagem();
        Picasso.with(aContext).load(url).into(imageView);


        container.addView(prestView);

        return  prestView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
