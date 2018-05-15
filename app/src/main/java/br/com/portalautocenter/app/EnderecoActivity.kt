package br.com.portalautocenter.app

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import br.com.portalautocenter.adapters.EnderecoAdapter
import br.com.portalautocenter.models.Endereco
import br.com.portalautocenter.models.Produto
import br.com.portalautocenter.utils.HttpConnection

import kotlinx.android.synthetic.main.activity_endereco.*
import kotlinx.android.synthetic.main.content_endereco.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import org.json.JSONArray

class EnderecoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_endereco)
        setSupportActionBar(toolbar)

        var intent = intent
        val idUsuario = intent.getIntExtra("idUsuario", 0)

        fab.setOnClickListener { view ->
            var intent = Intent(applicationContext, InsertEnderecoActivity::class.java)

            startActivity(intent)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val adapterEndereco = EnderecoAdapter(applicationContext, ArrayList<Endereco>())

        list_enderecos.adapter = adapterEndereco

        doAsync {
            var lstEnderecos:ArrayList<Endereco> = ArrayList<Endereco>()
            val jsonReturn = HttpConnection.get("http://10.107.144.17/inf4m/PortalAutoCenter/TCCPortalAutoCenter/api/endereco/select.php?idUsuario=$idUsuario")
            Log.d("TAG", jsonReturn)

            try{
                val jsonArray:JSONArray = JSONArray(jsonReturn)
                for (i in 0..jsonArray.length() step 1) run{
                    val e:Endereco = Endereco(jsonArray.getJSONObject(i).getInt("idEnderecoUsuario"), jsonArray.getJSONObject(i).getString("logradouro"),
                            jsonArray.getJSONObject(i).getString("numero"), jsonArray.getJSONObject(i).getString("complemento"),
                            jsonArray.getJSONObject(i).getString("bairro"), jsonArray.getJSONObject(i).getString("cep"),
                            jsonArray.getJSONObject(i).getInt("idTipoEndereco"),jsonArray.getJSONObject(i).getInt("idCidade"),
                            jsonArray.getJSONObject(i).getInt("idUsuario"), jsonArray.getJSONObject(i).getString("descricao"))
                    lstEnderecos.add(e)
                }
            }catch (e:Exception){
                Log.e("Erro: ", e.message)
            }

            uiThread {
                adapterEndereco.addAll(lstEnderecos)
            }
        }


        list_enderecos.setOnItemClickListener { adapterView, view, i, l ->
            toast(adapterEndereco.getItem(i).bairro)
        }
    }

}
