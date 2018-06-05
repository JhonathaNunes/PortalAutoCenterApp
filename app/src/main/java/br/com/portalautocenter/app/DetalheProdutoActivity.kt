package br.com.portalautocenter.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import br.com.portalautocenter.models.Produto
import br.com.portalautocenter.utils.api
import br.com.portalautocenter.utils.converteParaReal
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.activity_detalhe_produto.*
import kotlinx.android.synthetic.main.content_detalhe_produto.*
import org.json.JSONArray
import org.json.JSONObject

class DetalheProdutoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhe_produto)
        setSupportActionBar(toolbar)

        val intent = intent

        val produto:Produto = intent.getSerializableExtra("produto") as Produto

        txt_nome_produto.text = produto.nome
        txt_desc.text = produto.descricao
        val url = api + "cms/" + produto.imagem
        Picasso.with(applicationContext).load(url).into(img_produto)
        txt_preco.text = converteParaReal(produto.preco)
        txt_marca.text = produto.marca
        txt_fabricante.text = produto.fabricante
        txt_garantia.text = produto.garantia
        txt_obs.text = produto.obs

        fab.setOnClickListener { view ->
            val preference = getSharedPreferences("LOGADO", Context.MODE_PRIVATE)

            if (preference.getBoolean("STATUS", false)){
                val produtoJSON:JSONObject = JSONObject()
                produtoJSON.put("id", produto.id)
                produtoJSON.put("nome", produto.nome)
                produtoJSON.put("descricao", produto.descricao)
                produtoJSON.put("imagem", produto.imagem)
                produtoJSON.put("fabricante", produto.fabricante)
                produtoJSON.put("garantia", produto.garantia)
                produtoJSON.put("marca", produto.marca)
                produtoJSON.put("obs", produto.obs)
                produtoJSON.put("preco", produto.preco)
                produtoJSON.put("idFilial", produto.idFilial)

                val carrinho = JSONArray(preference.getString("CARRINHO", "[]"))

                carrinho.put(produtoJSON)

                preference.edit().putString("CARRINHO", carrinho.toString()).apply()
                finish()
            }else{
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
            }
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}
