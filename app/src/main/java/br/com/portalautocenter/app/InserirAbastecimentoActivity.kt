package br.com.portalautocenter.app

import android.arch.persistence.room.Room
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.portalautocenter.dao.AppDatabase
import br.com.portalautocenter.models.Abastecimento
import br.com.portalautocenter.utils.InputMask
import kotlinx.android.synthetic.main.activity_cadastro_usuario.*

import kotlinx.android.synthetic.main.activity_inserir_abastecimento.*
import kotlinx.android.synthetic.main.content_inserir_abastecimento.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import java.text.DateFormat
import java.util.*
import android.text.Editable
import android.text.TextWatcher
import java.text.NumberFormat


class InserirAbastecimentoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inserir_abastecimento)
        setSupportActionBar(toolbar)

        val locationManager = getSystemService(Context.LOCATION_SERVICE)

        val pref = getSharedPreferences("Veiculo", Context.MODE_PRIVATE)
        val idU = pref.getInt("idUsuario", 0)
        val idV = pref.getInt("idVeiculo", 0)
        var latitude = ""
        var longitude = ""

        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, getString(R.string.DATABASE_NAME)).allowMainThreadQueries().build()

        btn_inserir.setOnClickListener {
            /*Pegar latitude e longitude*/
            while (true){
                val listener = object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        latitude = location.getLatitude().toString()
                        longitude = location.getLongitude().toString()
                    }

                    override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {

                    }

                    override fun onProviderEnabled(s: String) {

                    }

                    override fun onProviderDisabled(s: String) {

                    }
                }
                break
            }

            val litros = txt_litros.text.toString().replace(",", ".").toDouble()
            val preco = txt_preco.text.toString().replace(",", ".").toDouble()
            val posto = txt_rede.text.toString()
            val tanque = txt_tanque.text.toString().replace(",", ".").toDouble()
            val data = DateFormat.getDateInstance().format(Date())

            val abastecimemto = Abastecimento(idU, idV, posto, "000", "000", data, litros, preco, tanque)

            db.abastecimentoDao().insertAll(abastecimemto)

            toast(getString(R.string.insert_abastecimento))

            finish()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}
