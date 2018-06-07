package br.com.portalautocenter.app

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import br.com.portalautocenter.utils.HttpConnection
import br.com.portalautocenter.utils.InputMask
import br.com.portalautocenter.utils.api

import kotlinx.android.synthetic.main.activity_pagamento.*
import kotlinx.android.synthetic.main.content_pagamento.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import org.json.JSONArray

class PagamentoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagamento)
        setSupportActionBar(toolbar)

        txt_cartao.addTextChangedListener(InputMask.mask(txt_cartao, InputMask.CREDIT_CARD))

        var intent = getIntent()

        var idUsuario = intent.getIntExtra("idUsuario", 0)

        var idEndereco = intent.getIntExtra("idEndereco", 0)

        val preference = getSharedPreferences("LOGADO", Context.MODE_PRIVATE)

        val jsonArray = JSONArray(preference.getString("CARRINHO", "[]"))
        Log.e("TAG", jsonArray.toString())

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btn_finaliza.setOnClickListener {
            doAsync {
                val url = api + "api/produtos/finalizarPedido.php"

                val map: HashMap<String, String> = hashMapOf("pedido" to jsonArray.toString(), "idUsuario" to idUsuario.toString(),
                        "idEndereco" to idEndereco.toString())

                val resultado = HttpConnection.post(url, map)
                Log.d("TAG", resultado)

                uiThread {
                    toast("Compra efetuada com sucesso! Aguarde a resposta da filial!")
                }
            }

            val preference = getSharedPreferences("LOGADO", Context.MODE_PRIVATE)
            preference.edit().putString("CARRINHO", "[]").apply()
            finish()
        }
    }

}
