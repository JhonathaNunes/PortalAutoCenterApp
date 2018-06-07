package br.com.portalautocenter.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
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

    var idUsuario = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrinho)
        setSupportActionBar(toolbar)

        val pref = getSharedPreferences("LOGADO", Context.MODE_PRIVATE)

        if (pref.getBoolean("STATUS", false) == false){
            AlertDialog.Builder(this)
                    .setCancelable(true)
                    .setTitle("Ação não disponível")
                    .setMessage("Você precisa estar logado para adcionar um produto ao carrinho.\n" +
                            "Deseja fazer o login?")
                    .setNegativeButton("Não", { dialogInterface, i ->
                        finish()
                    })
                    .setPositiveButton("Sim", { dialogInterface, i ->
                        var intent = Intent(applicationContext, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }).show()
            finish()
        }

        val list = list_compras

        var lstProdutos:ArrayList<Produto> = ArrayList<Produto>()

        val adapter = ProdutoAdapter(applicationContext, ArrayList<Produto>())

        list.adapter = adapter

        val preference = getSharedPreferences("LOGADO", Context.MODE_PRIVATE)

        val user = JSONObject(preference.getString("USUARIO", "[]"))
        idUsuario = user.getInt("idUsuario")

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

            val intent = Intent(applicationContext, EnderecoActivity::class.java)
            intent.putExtra("Compra", true)
            intent.putExtra("idUsuario", idUsuario)
            finish()
            startActivity(intent)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

}
