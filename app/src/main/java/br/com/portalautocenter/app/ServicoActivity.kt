package br.com.portalautocenter.app

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import br.com.portalautocenter.adapters.FilialAdapter
import br.com.portalautocenter.models.Filial
import br.com.portalautocenter.utils.HttpConnection
import br.com.portalautocenter.utils.api

import kotlinx.android.synthetic.main.activity_servico.*
import kotlinx.android.synthetic.main.content_servico.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class ServicoActivity : AppCompatActivity() {

    lateinit var datePickerDialog: DatePickerDialog

    lateinit var dateFormat: SimpleDateFormat

    var idServ = 0
    var idU = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_servico)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val intent = intent

        idServ = intent.getIntExtra("idServico", 0 )

        val usuario = getSharedPreferences("LOGADO", Context.MODE_PRIVATE)
        val str = JSONObject(usuario.getString("USUARIO", "NoUser"))

        idU = str.getInt("idUsuario")
        val adapterFilial = FilialAdapter(applicationContext, ArrayList<Filial>())

        list_filiais.adapter = adapterFilial

        dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

        doAsync {
            var lstFiliais:ArrayList<Filial> = ArrayList<Filial>()
            val jsonReturn = HttpConnection.get(api +"api/servicos/filialServico.php?idServico=$idServ")
            Log.d("TAG", jsonReturn)

            try{
                val jsonArray: JSONArray = JSONArray(jsonReturn)
                for (i in 0..jsonArray.length() step 1) run{
                    //ALTERAR SAPORRA NO ENDERECO
                    val e:Filial = Filial(jsonArray.getJSONObject(i).getInt("idFilial"), jsonArray.getJSONObject(i).getString("nomeFilial"),
                            jsonArray.getJSONObject(i).getString("logradouro") + ", " + jsonArray.getJSONObject(i).getString("numero") +
                                    " " + jsonArray.getJSONObject(i).getString("cidade") + " - " + jsonArray.getJSONObject(i).getString("sigla"))
                    lstFiliais.add(e)
                }
            }catch (e:Exception){
                Log.e("Erro: ", e.message)
            }

            uiThread {
                adapterFilial.addAll()
            }
        }

        list_filiais.setOnItemClickListener { adapterView, view, i, l ->
            setDateTimeField()

            datePickerDialog.show()
        }
    }

    private fun setDateTimeField() {
        val newCalendar = Calendar.getInstance()
        datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            val newDate = Calendar.getInstance()
            newDate.set(year, monthOfYear, dayOfMonth)
            val data:String = dateFormat.format(newDate.time)

            doAsync {
                val url = api + "api/servicos/visita.php"

                val map:HashMap<String, String> = hashMapOf("idUsuario" to idU.toString(), "idServico" to idServ.toString(),
                        "data" to dateFormat.format(newDate.time).toString())

                val resultado = HttpConnection.post(url, map)
                Log.d("TAG", resultado)


                uiThread {
                    val retorno = JSONObject(resultado)
                    val login_state = retorno.getBoolean("sucesso")
                    if (login_state == true){
                        toast("Visita marcada")
                        finish()
                    }else{
                        toast("Visita não marcada")
                        finish()
                    }
                }
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH))

    }

}
