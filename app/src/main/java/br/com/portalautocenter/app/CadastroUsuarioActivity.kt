package br.com.portalautocenter.app

import android.app.DatePickerDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
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


class CadastroUsuarioActivity : AppCompatActivity() {

    private val myCalendar = Calendar.getInstance()
    private var txt_data: EditText? = null

    var BRAZIL = Locale("pt", "BR")

    var senha:String = ""
    var s = Senha()

    var nome:String = ""
    var cpf:String = ""
    var email:String = ""
    var usuario:String = ""
    var dtNasc:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_usuario)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)

        /*Date picker*/
        val datePickerListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd/MM/yyyy"
            val sdf = SimpleDateFormat(myFormat, BRAZIL)
            txt_data!!.setText(sdf.format(myCalendar.getTime()))
        }


        txt_data = findViewById<View>(R.id.txt_data) as EditText
        txt_data!!.setOnClickListener(View.OnClickListener {
            DatePickerDialog(this, datePickerListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        })

        //Insere a máscara de CPF
        txt_cpf.addTextChangedListener(InputMask.mask(txt_cpf, InputMask.FORMAT_CPF))

        btn_cadastro.setOnClickListener {
            if (validaCampos()){
                doAsync {
                    val url ="http://10.107.144.17/inf4m/PortalAutoCenter/TCCPortalAutoCenter/api/usuario/inserir.php"
                    val map:HashMap<String, String> = hashMapOf("nome" to nome, "cpf" to cpf, "email" to email, "usuario" to usuario, "senha" to senha, "dtNasc" to dtNasc)
                    val resultado = HttpConnection.post(url, map)
                    val resultadoJson = JSONObject(resultado)
                    val sucesso = resultadoJson.getBoolean("sucesso")
                    uiThread {
                        if (sucesso){
                            toast("Usuário cadastrado com sucesso!")
                            finish()
                        }else{
                            toast("Falha no cadastro, tente novamente mais tarde!")
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

        if (senha.isEmpty()){
            txt_senha.setError(getString(R.string.erroVazio))
        }else if (s.validaSenha(senha) != true){
            txt_senha.setError(getString(R.string.formatoSenhaIncorreto))
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
}
