package br.com.portalautocenter.app

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import br.com.portalautocenter.models.Cidade
import br.com.portalautocenter.models.Estado
import br.com.portalautocenter.models.TipoEndereco
import br.com.portalautocenter.utils.HttpConnection
import br.com.portalautocenter.utils.InputMask

import kotlinx.android.synthetic.main.activity_insert_endereco.*
import kotlinx.android.synthetic.main.content_insert_endereco.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import org.json.JSONArray

class InsertEnderecoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_endereco)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        txt_cep.addTextChangedListener(InputMask.mask(txt_cep, InputMask.FORMAT_CEP))

        var adapterTP = ArrayAdapter<TipoEndereco>(this, android.R.layout.simple_spinner_dropdown_item)
        spinner_tpEndereco.adapter = adapterTP

        doAsync {
            var url = "http://10.107.144.17/Inf4m/PortalAutoCenter/TCCPortalAutoCenter/api/endereco/selecionarEndereco.php"

            var lst:ArrayList<TipoEndereco> = ArrayList<TipoEndereco>()
            val jsonReturn = HttpConnection.get(url)

            Log.d("TAG", jsonReturn)

            try {
                val jsonArray: JSONArray = JSONArray(jsonReturn)

                for (i in 0..jsonArray.length() step 1) run {
                    val t: TipoEndereco = TipoEndereco(jsonArray.getJSONObject(i).getInt("idTipoEndereco"), jsonArray.getJSONObject(i).getString("descricao"))

                    lst.add(t)

                }
            }catch (e:Exception) {
                Log.e("Cometeu um erro: ", e.message)
            }

            uiThread {
                adapterTP.addAll(lst)
            }
        }

        var adapterEstado = ArrayAdapter<Estado>(this, android.R.layout.simple_spinner_dropdown_item)
        spinner_estado.adapter = adapterEstado

        doAsync {
            var url = "http://10.107.144.17/Inf4m/PortalAutoCenter/TCCPortalAutoCenter/api/endereco/estados.php"

            var lst:ArrayList<Estado> = ArrayList<Estado>()
            val jsonReturn = HttpConnection.get(url)

            Log.d("TAG", jsonReturn)

            try {
                val jsonArray: JSONArray = JSONArray(jsonReturn)

                for (i in 0..jsonArray.length() step 1) run {
                    val estado: Estado = Estado(jsonArray.getJSONObject(i).getInt("idEstado"), jsonArray.getJSONObject(i).getString("sigla"))

                    lst.add(estado)

                }
            }catch (e:Exception) {
                Log.e("Errou Muito: ", e.message)
            }

            uiThread {
                adapterEstado.addAll(lst)
            }
        }

        val adapterCidade = ArrayAdapter<Cidade>(this, android.R.layout.simple_spinner_dropdown_item)
        spinner_cidade.adapter = adapterCidade

        spinner_estado?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                var idEstado = adapterEstado.getItem(position).id

                doAsync {
                    var url = "http://10.107.144.17/Inf4m/PortalAutoCenter/TCCPortalAutoCenter/api/endereco/cidades.php?idEstado=$idEstado"

                    var lst:ArrayList<Cidade> = ArrayList<Cidade>()
                    val jsonReturn = HttpConnection.get(url)

                    Log.d("TAG", jsonReturn)

                    try {
                        val jsonArray: JSONArray = JSONArray(jsonReturn)

                        for (i in 0..jsonArray.length() step 1) run {
                            val cidade: Cidade = Cidade(jsonArray.getJSONObject(i).getInt("idCidade"), jsonArray.getJSONObject(i).getString("nome"), jsonArray.getJSONObject(i).getInt("idEstado"))

                            lst.add(cidade)

                        }
                    }catch (e:Exception) {
                        Log.e("Errou Muito: ", e.message)
                    }

                    uiThread {
                        adapterCidade.clear()
                        adapterCidade.addAll(lst)
                    }
                }
            }
        }



    }

}