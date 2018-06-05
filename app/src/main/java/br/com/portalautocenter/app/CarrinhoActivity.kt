package br.com.portalautocenter.app

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import br.com.portalautocenter.adapters.ProdutoAdapter
import br.com.portalautocenter.models.Produto
import br.com.portalautocenter.utils.HttpConnection
import br.com.portalautocenter.utils.api

import kotlinx.android.synthetic.main.activity_carrinho.*
import kotlinx.android.synthetic.main.content_carrinho.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import org.json.JSONObject
import java.util.HashMap

class CarrinhoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrinho)
        setSupportActionBar(toolbar)

        val list = list_compras

        var lstProdutos:ArrayList<Produto> = ArrayList<Produto>()

        val adapter = ProdutoAdapter(applicationContext, ArrayList<Produto>())

        list.adapter = adapter

        val preference = getSharedPreferences("LOGADO", Context.MODE_PRIVATE)

        val user = JSONObject(preference.getString("USUARIO", "[]"))
        val idUsuario = user.getInt("idUsuario")

        val jsonArray = JSONArray(preference.getString("CARRINHO", "[]"))
        Log.e("TAG", jsonArray.toString())

        val tamanho = jsonArray.length()

        for (i in 0..tamanho-1 step 1) run {
            val p: Produto = Produto(jsonArray.getJSONObject(i).getInt("id"), jsonArray.getJSONObject(i).getString("nome"),
                    jsonArray.getJSONObject(i).getDouble("preco"), jsonArray.getJSONObject(i).getString("descricao"),
                    jsonArray.getJSONObject(i).getInt("idFilial"), jsonArray.getJSONObject(i).getString("imagem"),
                    jsonArray.getJSONObject(i).getString("marca"), jsonArray.getJSONObject(i).getString("fabricante"),
                    jsonArray.getJSONObject(i).getString("obs"), jsonArray.getJSONObject(i).getString("garantia"))

            lstProdutos.add(p)
        }

        adapter.addAll(lstProdutos)

        btn_finaliza.setOnClickListener {
            doAsync {
                val url = api + "api/usuario/inserir.php"

                val map: HashMap<String, String> = hashMapOf("pedido" to jsonArray.toString(), "idUsuario" to idUsuario.toString())

                val resultado = HttpConnection.post(url, map)
                Log.d("TAG", resultado)

                uiThread {
                    val retorno = JSONObject(resultado)
                    val resultado = retorno.getBoolean("sucesso")
                    if (resultado == true){
                        toast("Usuário cadastrado!")
                        finish()
                    }else{
                        toast("Usuario não cadastrado")
                    }
                }
            }
        }


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}
