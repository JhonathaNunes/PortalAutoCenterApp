package br.com.portalautocenter.app

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import br.com.portalautocenter.models.Cidade
import br.com.portalautocenter.models.Endereco
import br.com.portalautocenter.models.Estado
import br.com.portalautocenter.models.TipoEndereco
import br.com.portalautocenter.utils.HttpConnection
import br.com.portalautocenter.utils.InputMask
import br.com.portalautocenter.utils.api

import kotlinx.android.synthetic.main.activity_insert_endereco.*
import kotlinx.android.synthetic.main.content_insert_endereco.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import org.json.JSONObject
import java.util.HashMap

class InsertEnderecoActivity : AppCompatActivity() {
    var logradouro :String = ""
    var numero :String = ""
    var complemento :String = ""
    var bairro :String = ""
    var cep :String = ""
    var idTipoEndereco :Int = 0
    var idCidade :Int = 0
    var idUsuario :Int = 0

    var modo = ""
    var idEndereco = 0
    lateinit var endereco:Endereco

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_endereco)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        txt_cep.addTextChangedListener(InputMask.mask(txt_cep, InputMask.FORMAT_CEP))

        val intent = getIntent()
        modo = intent.getStringExtra("modo")
        if (modo == "insert"){
            idUsuario = intent.getIntExtra("idUsuario", 0)
            toast(idUsuario.toString())
        }else{
            endereco = intent.getSerializableExtra("endereco") as Endereco
            idUsuario = endereco.idEnderecoUsuario
            idEndereco = endereco.idEnderecoUsuario

            preencherCampos()
        }

        var adapterTP = ArrayAdapter<TipoEndereco>(this, android.R.layout.simple_spinner_dropdown_item)
        spinner_tpEndereco.adapter = adapterTP

        doAsync {
            var url = api+"api/endereco/selecionarEndereco.php"

            var lst:ArrayList<TipoEndereco> = ArrayList<TipoEndereco>()
            val jsonReturn = HttpConnection.get(url)

            Log.d("TAG", jsonReturn)

            try {
                val jsonArray: JSONArray = JSONArray(jsonReturn)

                for (i in 0..jsonArray.length() step 1) run {
                    val t: TipoEndereco = TipoEndereco(jsonArray.getJSONObject(i).getInt("idTipoEndereco"), jsonArray.getJSONObject(i).getString("descricao"))

                    lst.add(t)

                }
            }catch (e:Exception) {
                Log.e("Cometeu um erro: ", e.message)
            }

            uiThread {
                adapterTP.addAll(lst)
            }
        }
        spinner_tpEndereco?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                idTipoEndereco = adapterTP.getItem(position).id
            }
        }

        var adapterEstado = ArrayAdapter<Estado>(this, android.R.layout.simple_spinner_dropdown_item)
        spinner_estado.adapter = adapterEstado

        doAsync {
            var url = api + "api/endereco/estados.php"

            var lst:ArrayList<Estado> = ArrayList<Estado>()
            val jsonReturn = HttpConnection.get(url)

            Log.d("TAG", jsonReturn)

            try {
                val jsonArray: JSONArray = JSONArray(jsonReturn)

                for (i in 0..jsonArray.length() step 1) run {
                    val estado: Estado = Estado(jsonArray.getJSONObject(i).getInt("idEstado"), jsonArray.getJSONObject(i).getString("sigla"))

                    lst.add(estado)

                }
            }catch (e:Exception) {
                Log.e("Errou Muito: ", e.message)
            }

            uiThread {
                adapterEstado.addAll(lst)
            }
        }

        val adapterCidade = ArrayAdapter<Cidade>(this, android.R.layout.simple_spinner_dropdown_item)
        spinner_cidade.adapter = adapterCidade

        spinner_estado?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                var idEstado = adapterEstado.getItem(position).id

                doAsync {
                    var url = api + "api/endereco/cidades.php?idEstado=$idEstado"

                    var lst:ArrayList<Cidade> = ArrayList<Cidade>()
                    val jsonReturn = HttpConnection.get(url)

                    Log.d("TAG", jsonReturn)

                    try {
                        val jsonArray: JSONArray = JSONArray(jsonReturn)

                        for (i in 0..jsonArray.length() step 1) run {
                            val cidade: Cidade = Cidade(jsonArray.getJSONObject(i).getInt("idCidade"), jsonArray.getJSONObject(i).getString("nome"), jsonArray.getJSONObject(i).getInt("idEstado"))

                            lst.add(cidade)

                        }
                    }catch (e:Exception) {
                        Log.e("Errou Muito: ", e.message)
                    }

                    uiThread {
                        adapterCidade.clear()
                        adapterCidade.addAll(lst)
                    }
                }

                spinner_cidade?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                        idCidade = adapterCidade.getItem(position).id
                    }
                }
            }
        }

        btn_salvar.setOnClickListener {
            if (validaDados()){
                if (modo == "insert"){
                    doAsync {
                        val url = api + "api/endereco/inserir.php"

                        val map: HashMap<String, String> = hashMapOf("logradouro" to logradouro, "numero" to numero, "complemento" to complemento, "bairro" to bairro,
                                "cep" to cep, "idTipoEndereco" to idTipoEndereco.toString(), "idCidade" to idCidade.toString(), "idUsuario" to idUsuario.toString())

                        val resultado = HttpConnection.post(url, map)
                        Log.d("TAG", resultado)

                        uiThread {
                            val retorno = JSONObject(resultado)
                            val resultado = retorno.getBoolean("sucesso")
                            if (resultado == true){
                                toast("Endereço cadastrado!")
                                finish()
                            }else{
                                toast("Endereço não cadastrado")
                            }
                        }
                    }
                }else{
                    doAsync {
                        val url = api + "api/endereco/editar.php?idEndereco=$idEndereco"

                        val map: HashMap<String, String> = hashMapOf("logradouro" to logradouro, "numero" to numero, "complemento" to complemento, "bairro" to bairro,
                                "cep" to cep, "idTipoEndereco" to idTipoEndereco.toString(), "idCidade" to idCidade.toString(), "idUsuario" to idUsuario.toString())

                        val resultado = HttpConnection.post(url, map)
                        Log.d("TAG", resultado)

                        uiThread {
                            val retorno = JSONObject(resultado)
                            val resultado = retorno.getBoolean("sucesso")
                            if (resultado == true){
                                toast("Endereço editado!")
                                finish()
                            }else{
                                toast("Endereço não editado")
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (modo == "edit"){
            menuInflater!!.inflate(R.menu.menu_endereco, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.nav_excluir){


            /////FAZER A EXCLUSÃO E A API
            doAsync {
                val url ="http://10.107.144.17/inf4m/PortalAutoCenter/TCCPortalAutoCenter/api/endereco/excluir.php"

                val map: HashMap<String, String> = hashMapOf("idEndereco" to idEndereco.toString())

                val resultado = HttpConnection.post(url, map)
                Log.d("TAG", resultado)

                uiThread {
                    val retorno = JSONObject(resultado)
                    val resultado = retorno.getBoolean("sucesso")
                    if (resultado == true){
                        toast("Endereço excluido!")
                        finish()
                    }else{
                        toast("Endereço não excluido")
                    }
                }
            }
        }

        when (item.itemId) {
            android.R.id.home  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
            -> {
                finish()
            }
            else -> {
            }
        }

        return true
    }

    fun validaDados():Boolean{
        var valido = true

        if (txt_logradouro.text.toString().isEmpty()){
            txt_logradouro.setError("Preencha este campo")
            valido = false
        }else{
            logradouro = txt_logradouro.text.toString()
        }

        if (txt_numero.text.toString().isEmpty()){
            txt_numero.setError("Preencha este campo")
            valido = false
        }else{
            numero = txt_numero.text.toString()
        }

        if (txt_complemento.text.toString().isEmpty() == false){
            complemento = txt_complemento.text.toString()
        }

        if (txt_bairro.text.toString().isEmpty()){
            txt_bairro.setError("Preencha este campo")
            valido = false
        }else{
            bairro = txt_bairro.text.toString()
        }

        if (txt_cep.text.toString().isEmpty()){
            txt_cep.setError("Preencha este campo")
            valido = false
        }else{
            cep = txt_cep.text.toString()
        }
        return valido
    }

    fun preencherCampos(){
        txt_logradouro.setText(endereco.logradouro)
        txt_numero.setText(endereco.numero)
        txt_complemento.setText(endereco.complemento)
        txt_bairro.setText(endereco.bairro)
        txt_cep.setText(endereco.cep)
    }
}