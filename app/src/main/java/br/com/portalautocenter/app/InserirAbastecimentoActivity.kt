package br.com.portalautocenter.app

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.portalautocenter.models.Abastecimento

import kotlinx.android.synthetic.main.activity_inserir_abastecimento.*
import kotlinx.android.synthetic.main.content_inserir_abastecimento.*
import org.jetbrains.anko.toast
import java.text.DateFormat
import java.util.*

class InserirAbastecimentoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inserir_abastecimento)
        setSupportActionBar(toolbar)

        val pref = getSharedPreferences("Veiculo", Context.MODE_PRIVATE)
        val idU = pref.getInt("idUsuario", 0)
        val idV = pref.getInt("idVeiculo", 0)

        btn_inserir.setOnClickListener {

            val litros = txt_litros.text.toString().replace(",", ".").toDouble()
            val preco = txt_preco.text.toString().replace(",", ".").toDouble()
            val posto = txt_rede.text.toString()
            val tanque = txt_tanque.text.toString().replace(",", ".").toDouble()
            val data = DateFormat.getDateInstance().format(Date())
            toast(data)

            val abastecimemto = Abastecimento(idU, idV, posto, "000", "000", data, litros, preco, tanque)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}
