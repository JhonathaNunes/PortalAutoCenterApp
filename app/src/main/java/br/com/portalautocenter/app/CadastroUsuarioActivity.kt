package br.com.portalautocenter.app

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import br.com.portalautocenter.utils.HttpConnection
import br.com.portalautocenter.utils.InputMask
import br.com.portalautocenter.utils.Senha
import kotlinx.android.synthetic.main.activity_cadastro_usuario.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import java.util.*
import java.text.SimpleDateFormat
import android.widget.DatePicker
import android.widget.TextView
import com.squareup.picasso.Picasso

class CadastroUsuarioActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var datePickerDialog: DatePickerDialog

    lateinit var dateFormat: SimpleDateFormat

    var senha:String = ""
    var s = Senha()

    var nome:String = ""
    var cpf:String = ""
    var email:String = ""
    var usuario:String = ""
    var dtNasc:String = ""
    var edicao:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_usuario)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)

        var intent = intent

        edicao = intent.getBooleanExtra("Edicao", false)

        dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)

        setDateTimeField();

        datePickerDialog.getDatePicker().setMaxDate(Date().time)
        //datePickerDialog.getDatePicker().setMinDate(Date().time)

        val date = System.currentTimeMillis()
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val dateString = sdf.format(date)
        txt_data.setText(dateString)

        if (edicao){
            txt_senha.visibility = TextView.GONE
            val usuario = getSharedPreferences("LOGADO", Context.MODE_PRIVATE)

            if (usuario.getBoolean("STATUS", false)){
                preencherCampos(usuario.getString("USUARIO", "NoUser"))
            }
        }

        //Insere a máscara de CPF
        txt_cpf.addTextChangedListener(InputMask.mask(txt_cpf, InputMask.FORMAT_CPF))

        btn_cadastro.setOnClickListener {
            if (validaCampos()){

                if (edicao){
                    val idUsuario = intent.getIntExtra("idUsuario", 0)

                    doAsync {
                        val url ="http://www.i9autocenter/api/usuario/editar.php"
//                        val url ="http://10.107.144.17/inf4m/PortalAutoCenter/TCCPortalAutoCenter/api/usuario/editar.php"

                        val map:HashMap<String, String> = hashMapOf("nome" to nome, "cpf" to cpf, "email" to email,
                                "usuario" to usuario, "dtNasc" to dtNasc, "idUsuario" to idUsuario.toString())

                        val resultado = HttpConnection.post(url, map)
                        Log.d("TAG", resultado)

                        uiThread {
                            val retorno = JSONObject(resultado)
                            val resultado = retorno.getBoolean("sucesso")
                            if (resultado == true){
                                toast("Usuário Atualizado!")
                                finish()
                            }else{
                                toast("Usuario não Atualizado")
                            }
                        }
                    }

                }else{
                    doAsync {
                        val url ="http://10.107.144.17/inf4m/PortalAutoCenter/TCCPortalAutoCenter/api/usuario/inserir.php"

                        val map:HashMap<String, String> = hashMapOf("nome" to nome, "cpf" to cpf, "email" to email,
                                "usuario" to usuario, "senha" to senha, "dtNasc" to dtNasc)

                        val resultado = HttpConnection.post(url, map)
                        Log.d("TAG", resultado)

                        uiThread {
                            val retorno = JSONObject(resultado)
                            val resultado = retorno.getBoolean("sucesso")
                            if (resultado == true){
                                toast("Usuário cadastrado!")
                                finish()
                            }else{
                                toast("Usuario não cadastrado")
                            }
                        }
                    }
                }
            }
        }
    }

    fun validaCampos():Boolean {
        var valido = true
        senha = txt_senha.text.toString()

        if (txt_nome.text.toString().isEmpty()){
            txt_nome.setError("Digite o seu nome")
            valido = false
        }else{
            nome = txt_nome.text.toString()

        }

        if (txt_cpf.text.toString().isEmpty() or (txt_cpf.text.toString().length < 14)){
            txt_cpf.setError("Digite o CPF")
            valido = false
        }else{
            cpf = txt_cpf.text.toString()
        }

        if (txt_email.text.toString().isEmpty()){
            txt_email.setError("Digite o e-mail")
            valido = false
        }else{
            email = txt_email.text.toString()
        }

        if (txt_usuario.text.toString().isEmpty()){
            txt_usuario.setError("Digite um nome de usuário")
            valido = false
        }else{
            usuario = txt_usuario.text.toString()
        }

        if (txt_data!!.text.toString().isEmpty()){
            txt_data!!.setError("Selecione uma Data de Nascimento")
            valido = false

        }else{
            dtNasc = txt_data!!.text.toString()
        }

        if (edicao == false){
            if (senha.isEmpty()){
                txt_senha.setError(getString(R.string.erroVazio))
                valido = false
            }else if (s.validaSenha(senha) != true){
                txt_senha.setError(getString(R.string.formatoSenhaIncorreto))
                valido = false
            }
        }

        return valido
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home
            -> {
                finish()
            }
            else -> {
            }
        }
        return true
    }

    override fun onClick(view: View?) {
        datePickerDialog.show()
    }

    private fun setDateTimeField() {
        txt_data.setOnClickListener(this)

        val newCalendar = Calendar.getInstance()
        datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            val newDate = Calendar.getInstance()
            newDate.set(year, monthOfYear, dayOfMonth)
            txt_data.setText(dateFormat.format(newDate.time))
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH))
    }

    private fun preencherCampos(json:String){
        val usuario = JSONObject(json)

        txt_nome.setText(usuario.getString("nome"))
        txt_cpf.setText(usuario.getString("cpf"))
        txt_email.setText(usuario.getString("email"))
        txt_data.setText(usuario.getString("dtNasc"))
        txt_usuario.setText(usuario.getString("usuario"))
        val url = "http://10.107.144.17/inf4m/PortalAutoCenter/TCCPortalAutoCenter/"+usuario.getString("fotoUser")
        Picasso.with(applicationContext).load(url).into(profile_image)
    }
}