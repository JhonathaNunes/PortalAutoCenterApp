package br.com.portalautocenter.app

import android.content.Context
import android.content.Intent
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import br.com.portalautocenter.adapters.ProdutoAdapter
import br.com.portalautocenter.adapters.ServicoAdapter
import br.com.portalautocenter.models.Produto
import br.com.portalautocenter.models.Servico
import br.com.portalautocenter.utils.HttpConnection

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_produtos.view.*
import kotlinx.android.synthetic.main.fragment_servicos.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import br.com.portalautocenter.adapters.PrestadoraAdapter
import br.com.portalautocenter.adapters.ProdutosDestaqueAdapter
import br.com.portalautocenter.models.Prestadora
import br.com.portalautocenter.utils.api
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.android.synthetic.main.fragment_produtos.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{
    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        val usuario = getSharedPreferences("LOGADO", Context.MODE_PRIVATE)

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

//Abri o navigation Drawer
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        if (usuario.getBoolean("STATUS", false)){
            nav_view.inflateMenu(R.menu.menu_logado)
        }else{
            nav_view.inflateMenu(R.menu.menu_padrao)
        }

        nav_view.setNavigationItemSelectedListener(this)
    }

    //Navigation Drawer
    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.

        when (item.itemId) {
            R.id.nav_login -> {
                //Refresh na activity
                finish()
                startActivity(getIntent())
                //Abre a tela de login
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_perfli -> {
                val intent = Intent(applicationContext, PerfilActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_abastecimento -> {
                val intent = Intent(applicationContext, CarrosAbastecimentoActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_pedidos -> {
                val intent = Intent(applicationContext, PedidosActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_carrinho -> {
                startActivity(Intent(applicationContext, CarrinhoActivity::class.java))
            }
            R.id.nav_logout -> {
                deletarSharedPreferences()
                //Refresh na activity
                finish()
                startActivity(getIntent())

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1)
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return 3
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    class PlaceholderFragment : Fragment() {

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            var rootView = inflater.inflate(R.layout.fragment_main, container, false)

            /*Preencher Carousel de prestadora*/

            val view_prestadora = rootView.view_prestadora
            val produtos = rootView.view_produtos

            doAsync {
                var lstPrestadora:ArrayList<Prestadora> = ArrayList<Prestadora>()
                val jsonReturn = HttpConnection.get(api +  "api/prestadora/selecionar.php")

                Log.d("TAG", jsonReturn)

                try {
                    val jsonArray:JSONArray = JSONArray(jsonReturn)

                    for (i in 0..jsonArray.length() step 1) run {
                        val p: Prestadora = Prestadora(jsonArray.getJSONObject(i).getInt("idPrestadora"), jsonArray.getJSONObject(i).getString("nomeFantasia"), jsonArray.getJSONObject(i).getString("fotoPrestadora"),
                                jsonArray.getJSONObject(i).getString("descricao"), jsonArray.getJSONObject(i).getString("telefone"), jsonArray.getJSONObject(i).getString("logradouro"),
                                jsonArray.getJSONObject(i).getString("numero"), jsonArray.getJSONObject(i).getString("bairro"), jsonArray.getJSONObject(i).getString("referencia"),
                                jsonArray.getJSONObject(i).getString("cep"), jsonArray.getJSONObject(i).getString("cidade"), jsonArray.getJSONObject(i).getString("estado"))

                        lstPrestadora.add(p)

                    }
                }catch (e:Exception) {
                    Log.e("Cometeu um erro: ", e.message)
                }
                uiThread {
                    val customAdapter = PrestadoraAdapter(context, lstPrestadora)
                    view_prestadora.adapter = customAdapter
                }

                /*Preencher Carousel de Produtos*/
                doAsync {
                    var lstProdutos:ArrayList<Produto> = ArrayList<Produto>()
                    val jsonReturn = HttpConnection.get(api + "api/produtos/selecionar.php")

                    Log.d("TAG", jsonReturn)

                    try {
                        val jsonArray:JSONArray = JSONArray(jsonReturn)

                        for (i in 0..jsonArray.length() step 1) run {
                            val p: Produto = Produto(jsonArray.getJSONObject(i).getInt("idProduto"), jsonArray.getJSONObject(i).getString("nome"),
                                    jsonArray.getJSONObject(i).getDouble("preco"), jsonArray.getJSONObject(i).getString("descricao"),
                                    jsonArray.getJSONObject(i).getInt("idFilial"), jsonArray.getJSONObject(i).getString("imagem"),
                                    jsonArray.getJSONObject(i).getString("marca"), jsonArray.getJSONObject(i).getString("fabricante"),
                                    jsonArray.getJSONObject(i).getString("obs"), jsonArray.getJSONObject(i).getString("garantia"))

                            lstProdutos.add(p)

                        }
                    }catch (e:Exception){
                        Log.e("Cometeu um erro: ", e.message)
                    }

                    uiThread {
                        val produtoAdapter = ProdutosDestaqueAdapter(context, lstProdutos)
                        produtos.adapter = produtoAdapter
                    }
                }
            }

            if (arguments.getInt(ARG_SECTION_NUMBER)==2){

                val adapterProduto = ProdutoAdapter(context, ArrayList<Produto>());
                rootView = inflater.inflate(R.layout.fragment_produtos, container, false)
                rootView.list_produtos.adapter = adapterProduto

                rootView.btn_compra.setOnClickListener {
                    startActivity(Intent(context, CarrinhoActivity::class.java))
                }

                doAsync {
                    var lstProdutos:ArrayList<Produto> = ArrayList<Produto>()
                    val jsonReturn = HttpConnection.get(api + "api/produtos/selecionar.php")

                    Log.d("TAG", jsonReturn)

                    try {
                        val jsonArray:JSONArray = JSONArray(jsonReturn)

                        for (i in 0..jsonArray.length() step 1) run {
                            val p: Produto = Produto(jsonArray.getJSONObject(i).getInt("idProduto"), jsonArray.getJSONObject(i).getString("nome"),
                                    jsonArray.getJSONObject(i).getDouble("preco"), jsonArray.getJSONObject(i).getString("descricao"),
                                    jsonArray.getJSONObject(i).getInt("idFilial"), jsonArray.getJSONObject(i).getString("imagem"),
                                    jsonArray.getJSONObject(i).getString("marca"), jsonArray.getJSONObject(i).getString("fabricante"),
                                    jsonArray.getJSONObject(i).getString("obs"), jsonArray.getJSONObject(i).getString("garantia"))

                            lstProdutos.add(p)

                        }
                    }catch (e:Exception){
                        Log.e("Cometeu um erro: ", e.message)
                    }

                    uiThread {
                        adapterProduto.addAll(lstProdutos)
                    }

                    rootView.list_produtos.setOnItemClickListener { adapterView, view, i, l ->
                        val intent = Intent(context, DetalheProdutoActivity::class.java)

                        intent.putExtra("produto", adapterProduto.getItem(i))

                        startActivity(intent)
                    }
                }

            } else if(arguments.getInt(ARG_SECTION_NUMBER)==3){
                val adapterServico = ServicoAdapter(context, ArrayList<Servico>())

                rootView = inflater.inflate(R.layout.fragment_servicos, container, false)
                rootView.list_servicos.adapter = adapterServico

                doAsync {
                    var lstServico:ArrayList<Servico> = ArrayList<Servico>()
                    val jsonReturn = HttpConnection.get(api + "api/servicos/selecionar.php")

                    Log.d("TAG", jsonReturn)

                    try {
                        val jsonArray:JSONArray = JSONArray(jsonReturn)

                        for (i in 0..jsonArray.length() step 1){
                            val s: Servico = Servico(jsonArray.getJSONObject(i).getInt("idServico"), jsonArray.getJSONObject(i).getString("nome"),
                                    jsonArray.getJSONObject(i).getString("descricao"), jsonArray.getJSONObject(i).getString("imagem"))

                            lstServico.add(s)
                        }
                    }catch (e:Exception){
                        Log.e("Erro: ", e.message)
                    }

                    uiThread {
                        adapterServico.addAll(lstServico)
                    }
                }

                rootView.list_servicos.setOnItemClickListener { adapterView, view, i, l ->
                    val intent = Intent(context, ServicoActivity::class.java)
                    intent.putExtra("idServico", adapterServico.getItem(i).id)
                    startActivity(intent)
                }
            }

            return rootView
        }

        override fun onResume() {

            super.onResume()
        }

        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private val ARG_SECTION_NUMBER = "section_number"

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(sectionNumber: Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }

    fun deletarSharedPreferences() {
        val pref = getSharedPreferences("LOGADO", Context.MODE_PRIVATE)
        pref.edit().clear().apply()
    }
}
