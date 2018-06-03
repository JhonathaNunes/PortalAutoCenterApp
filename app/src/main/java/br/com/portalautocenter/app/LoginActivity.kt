package br.com.portalautocenter.app

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.MenuItem
import br.com.portalautocenter.models.Usuario
import br.com.portalautocenter.utils.HttpConnection
import br.com.portalautocenter.utils.api
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val preferences = getSharedPreferences("LOGADO", Context.MODE_PRIVATE)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)


        btn_login.setOnClickListener {
            val usuario = txt_usuario.text.toString()
            val senha = txt_senha.text.toString()

            doAsync {
                val url = api + "api/usuario/login.php"

                val map:HashMap<String, String> = hashMapOf("usuario" to usuario, "senha" to senha)

                val resultado = HttpConnection.post(url, map)
                Log.d("TAG", resultado)


                uiThread {
                    val retorno = JSONObject(resultado)
                    val login_state = retorno.getBoolean("sucesso")
                    if (login_state == true){
                        val usuario = retorno.getJSONObject("usuario")
                        val carrinhoArray:JSONArray = JSONArray()
                        preferences.edit().putString("USUARIO", usuario.toString()).apply()
                        preferences.edit().putBoolean("STATUS", true).apply()
                        preferences.edit().putString("CARRINHO", carrinhoArray.toString()).apply()
                        finish()
                    }else{
                        limparTextos()
                        toast("Usuario ou senha incorretos")
                    }
                }
            }
        }

        txt_cadastro.setOnClickListener {
            val intent = Intent(applicationContext, CadastroUsuarioActivity::class.java)
            startActivity(intent)
        }
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

    fun limparTextos(){
        txt_usuario.setText("")
        txt_senha.setText("")
    }

    fun deletarSharedPreferences() {
        val pref = getSharedPreferences("LOGADO", Context.MODE_PRIVATE)
        pref.edit().clear().apply()
    }
}
