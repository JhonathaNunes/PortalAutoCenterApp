package br.com.portalautocenter.app

import android.content.Intent
import android.os.AsyncTask
import android.support.design.widget.TabLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.android.synthetic.main.fragment_produtos.view.*
import kotlinx.android.synthetic.main.fragment_servicos.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

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

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_settings) {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            return true
        }

        return super.onOptionsItemSelected(item)
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

            if (arguments.getInt(ARG_SECTION_NUMBER)==2){

                val adapterProduto = ProdutoAdapter(context, ArrayList<Produto>());
                rootView = inflater.inflate(R.layout.fragment_produtos, container, false)
                rootView.list_produtos.adapter = adapterProduto

                doAsync {
                    var lstProdutos:ArrayList<Produto> = ArrayList<Produto>()
                    val jsonReturn = HttpConnection.get("http://10.0.2.2/inf4m/portal/api/produtos/selecionar.php")

                    Log.d("TAG", jsonReturn)

                    try {
                        val jsonArray:JSONArray = JSONArray(jsonReturn)

                        for (i in 0..jsonArray.length() step 1) run {
                            val p:Produto = Produto(jsonArray.getJSONObject(i).getInt("idProduto"), jsonArray.getJSONObject(i).getString("nome"),
                                    jsonArray.getJSONObject(i).getDouble("preco"), jsonArray.getJSONObject(i).getString("descricao"),
                                    jsonArray.getJSONObject(i).getInt("idSubcategoria"), jsonArray.getJSONObject(i).getInt("idMarcaProduto"),
                                    jsonArray.getJSONObject(i).getInt("idFilial"), jsonArray.getJSONObject(i).getString("imagem"))

                            lstProdutos.add(p)

                        }
                    }catch (e:Exception){
                        Log.e("Cometeu um erro: ", e.message)
                    }

                    uiThread {
                        adapterProduto.addAll(lstProdutos)
                    }
                }

            } else if(arguments.getInt(ARG_SECTION_NUMBER)==3){
                val adapterServico = ServicoAdapter(context, ArrayList<Servico>())

                rootView = inflater.inflate(R.layout.fragment_servicos, container, false)
                rootView.list_servicos.adapter = adapterServico

                doAsync {
                    var lstServico:ArrayList<Servico> = ArrayList<Servico>()
                    val jsonReturn = HttpConnection.get("http://10.0.2.2/inf4m/portal/api/servicos/selecionar.php")

                    Log.d("TAG", jsonReturn)

                    try {
                        val jsonArray:JSONArray = JSONArray(jsonReturn)

                        for (i in 0..jsonArray.length() step 1){
//                            idServico, nome, descricao, imagem
                            val s:Servico = Servico(jsonArray.getJSONObject(i).getInt("idServico"), jsonArray.getJSONObject(i).getString("nome"),
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
            }

            return rootView
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
}
