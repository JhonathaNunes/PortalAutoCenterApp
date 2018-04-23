package br.com.portalautocenter.app

import android.arch.persistence.room.Room
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.portalautocenter.adapters.AbastecimentoAdapter
import br.com.portalautocenter.dao.AppDatabase
import br.com.portalautocenter.models.Abastecimento

import kotlinx.android.synthetic.main.activity_controle_abastecimento.*
import kotlinx.android.synthetic.main.content_controle_abastecimento.*
import org.jetbrains.anko.toast

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

        val adapter = AbastecimentoAdapter(applicationContext, ArrayList<Abastecimento>())

        list_abastecimentos.adapter = adapter

        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, getString(R.string.DATABASE_NAME)).allowMainThreadQueries().build()

        val list_abastecimentos = db.abastecimentoDao().all

        adapter.addAll(list_abastecimentos)

        super.onResume()
    }

}
