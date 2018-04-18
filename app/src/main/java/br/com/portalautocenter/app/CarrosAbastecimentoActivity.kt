package br.com.portalautocenter.app

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import br.com.portalautocenter.adapters.VeiculosAbastecimentoAdapter
import br.com.portalautocenter.models.Produto
import br.com.portalautocenter.models.Veiculo
import br.com.portalautocenter.utils.HttpConnection

import kotlinx.android.synthetic.main.activity_carros_abastecimento.*
import kotlinx.android.synthetic.main.content_carros_abastecimento.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import org.json.JSONObject

class CarrosAbastecimentoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val usuario = getSharedPreferences("LOGADO", Context.MODE_PRIVATE)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carros_abastecimento)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val adapter = VeiculosAbastecimentoAdapter(applicationContext, ArrayList<Veiculo>())

        list_carro.adapter = adapter

        val id = JSONObject(usuario.getString("USUARIO", "null"))

        doAsync {
            val listVeiculos = ArrayList<Veiculo>()
            val jsonReturn = HttpConnection.get("http://10.0.2.2/inf4m/PortalAutoCenter/TCCPortalAutoCenter/api/veiculos/selecionar.php?idUsuario=" +
                    "${id.getInt("idUsuario")}")

            Log.d("TAG", jsonReturn)

            try {
                val jsonArray: JSONArray = JSONArray(jsonReturn)

                for (i in 0..jsonArray.length() step 1) run {
                    val v = Veiculo(jsonArray.getJSONObject(i).getInt("idVeiculo"), jsonArray.getJSONObject(i).getString("placa"), jsonArray.getJSONObject(i).getString("ano"),
                            jsonArray.getJSONObject(i).getString("cor"), jsonArray.getJSONObject(i).getInt("idModelo"), jsonArray.getJSONObject(i).getInt("idUsuario"),
                            jsonArray.getJSONObject(i).getString("acessorios"), jsonArray.getJSONObject(i).getString("modelo"), jsonArray.getJSONObject(i).getString("marca"))

                    listVeiculos.add(v)

                }
            }catch (e:Exception){
                Log.e("Cometeu um erro: ", e.message)
            }

            uiThread {
                adapter.addAll(listVeiculos)
            }
        }
    }

}
