package br.com.portalautocenter.app

import android.arch.persistence.room.Room
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.portalautocenter.adapters.AbastecimentoAdapter
import br.com.portalautocenter.dao.AppDatabase
import br.com.portalautocenter.models.Abastecimento

import kotlinx.android.synthetic.main.activity_controle_abastecimento.*
import kotlinx.android.synthetic.main.content_controle_abastecimento.*


class ControleAbastecimentoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_controle_abastecimento)
        setSupportActionBar(toolbar)

        btn_inserir.setOnClickListener {
            val intent = Intent(applicationContext, InserirAbastecimentoActivity::class.java)
            startActivity(intent)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onResume() {
        val veiculo = getSharedPreferences("Veiculo", Context.MODE_PRIVATE)

        val adapter = AbastecimentoAdapter(applicationContext, ArrayList<Abastecimento>())

        list_abastecimentos.adapter = adapter

        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, getString(R.string.DATABASE_NAME)).allowMainThreadQueries().build()

        val lst_abastecimentos = db.abastecimentoDao().getByUser(veiculo.getInt("idUsuario", 0), veiculo.getInt("idVeiculo", 0))

        adapter.addAll(lst_abastecimentos)

        list_abastecimentos.setOnItemClickListener { adapterView, view, i, l ->
            val intent = Intent(applicationContext, InserirAbastecimentoActivity::class.java)

            intent.putExtra("viewMode", true)
            intent.putExtra("Abastecimento", adapter.getItem(i))

            startActivity(intent)
        }


        super.onResume()
    }
}
