package br.com.portalautocenter.app

import android.arch.persistence.room.Room
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import br.com.portalautocenter.dao.AppDatabase
import br.com.portalautocenter.models.Abastecimento

import kotlinx.android.synthetic.main.activity_inserir_abastecimento.*
import kotlinx.android.synthetic.main.content_inserir_abastecimento.*
import org.jetbrains.anko.toast
import java.text.DateFormat
import java.util.*
import android.util.Log
import br.com.portalautocenter.utils.MonetaryMask
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng

class InserirAbastecimentoActivity : AppCompatActivity(), OnMapReadyCallback{
    var locationManager : LocationManager? = null
    var latitude:Double = 0.0
    var longitude:Double = 0.0

    lateinit var gMap:GoogleMap

    val MAP_BIEW_BUNDLE_KEY:String = "MapViewBundleKey"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inserir_abastecimento)
        setSupportActionBar(toolbar)
        
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager?

        val locale = Locale("pt", "BR")
        txt_preco.addTextChangedListener(MonetaryMask(txt_preco, locale))

        val pref = getSharedPreferences("Veiculo", Context.MODE_PRIVATE)
        val idU = pref.getInt("idUsuario", 0)
        val idV = pref.getInt("idVeiculo", 0)

        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, getString(R.string.DATABASE_NAME)).allowMainThreadQueries().build()

        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), Context.CONTEXT_INCLUDE_CODE)

        var mapViewBundle: Bundle? = null

        if (savedInstanceState != null){
            mapViewBundle = savedInstanceState.getBundle(MAP_BIEW_BUNDLE_KEY)
        }

        mapa_abastecimento.onCreate(mapViewBundle)
        mapa_abastecimento.getMapAsync(this)

        btn_inserir.setOnClickListener {
            try {
                locationManager?.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, locationListener, mainLooper)
            }catch (ex: SecurityException){
                Log.d("LocationTAG", "Security Exception, no location available")
            }

            val litros = txt_litros.text.toString().replace(",", ".").toDouble()
            val preco = txt_preco.text.toString().replace(".", "").replace(",", ".").replace("R$", "").toDouble()
            val posto = txt_rede.text.toString()
            val tanque = txt_tanque.text.toString().replace(",", ".").toDouble()
            val data = DateFormat.getDateInstance().format(Date())

            toast(preco.toString())
            val abastecimemto = Abastecimento(idU, idV, posto, latitude, longitude, data, litros, preco, tanque)

            db.abastecimentoDao().insertAll(abastecimemto)

            toast(getString(R.string.insert_abastecimento))

            finish()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onResume() {
        super.onResume()

        var gpsEnabled = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (gpsEnabled == false){
            AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle("GPS desligado")
                    .setMessage("Não será possível inserir o abastecimento com o GPS desativado.\n" +
                            "Deseja ativar o GPS?")
                    .setNegativeButton("Não", { dialogInterface, i ->
                        finish()
                    })
                    .setPositiveButton("Sim", { dialogInterface, i ->
                        startActivity(getIntent())
                        var intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        startActivity(intent)
                        finish()
                    }).show()
        }

        mapa_abastecimento.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapa_abastecimento.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapa_abastecimento.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapa_abastecimento.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapa_abastecimento.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        mapa_abastecimento.onPause()
    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            longitude = location.longitude
            latitude = location.latitude
        }
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap
        gMap.setMinZoomPreference(12F)
        val mark = LatLng(48.857482, 2.348088)
        gMap.moveCamera(CameraUpdateFactory.newLatLng(mark))
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)


        var mapViewBundle = outState!!.getBundle(MAP_BIEW_BUNDLE_KEY)
        if (mapViewBundle == null){
            mapViewBundle = Bundle()
            outState.putBundle(MAP_BIEW_BUNDLE_KEY, mapViewBundle)
        }

        mapa_abastecimento.onSaveInstanceState(mapViewBundle)
    }
}
