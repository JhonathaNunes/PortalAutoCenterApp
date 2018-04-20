package br.com.portalautocenter.app

import android.arch.persistence.room.Room
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import br.com.portalautocenter.dao.AppDatabase

import kotlinx.android.synthetic.main.activity_controle_abastecimento.*

class ControleAbastecimentoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_controle_abastecimento)
        setSupportActionBar(toolbar)

        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, getString(R.string.DATABASE_NAME)).build()

        btn_inserir.setOnClickListener {
            val intent = Intent(applicationContext, InserirAbastecimentoActivity::class.java)
            startActivity(intent)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}
