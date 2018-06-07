package br.com.portalautocenter.app

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ArrayAdapter
import br.com.portalautocenter.adapters.PedidoAdapter
import br.com.portalautocenter.models.Pedido
import br.com.portalautocenter.utils.HttpConnection
import br.com.portalautocenter.utils.api

import kotlinx.android.synthetic.main.activity_pedidos.*
import kotlinx.android.synthetic.main.content_pedidos.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList

class PedidosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pedidos)
        setSupportActionBar(toolbar)

        val usuario = getSharedPreferences("LOGADO", Context.MODE_PRIVATE)
        val str = JSONObject(usuario.getString("USUARIO", "NoUser"))

        var idU = str.getInt("idUsuario")

        val adapter = PedidoAdapter(applicationContext, ArrayList<Pedido>())
        list_pedidos.adapter = adapter

        doAsync {
            var lstPedido: ArrayList<Pedido> = ArrayList<Pedido>()
            val jsonReturn = HttpConnection.get(api +"api/produtos/pedidos.php?idU=$idU")
            Log.d("TAG", jsonReturn)

            try{
                val jsonArray: JSONArray = JSONArray(jsonReturn)
                for (i in 0..jsonArray.length() step 1) run{
                    //ALTERAR SAPORRA NO ENDERECO
                    val e:Pedido = Pedido(jsonArray.getJSONObject(i).getInt("idPedido"), jsonArray.getJSONObject(i).getString("nome"),
                            jsonArray.getJSONObject(i).getString("status"))
                    lstPedido.add(e)
                }
            }catch (e:Exception){
                Log.e("Erro: ", e.message)
            }

            uiThread {
                adapter.addAll()
            }
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}
