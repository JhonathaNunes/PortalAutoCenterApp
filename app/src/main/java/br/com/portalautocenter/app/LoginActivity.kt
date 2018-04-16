package br.com.portalautocenter.app

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.content.SharedPreferences
import android.view.MenuItem
import br.com.portalautocenter.models.Usuario
import br.com.portalautocenter.utils.HttpConnection
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
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
                val url ="http://10.0.2.2/inf4m/portal/api/usuario/login.php"

                val map:HashMap<String, String> = hashMapOf("usuario" to usuario, "senha" to senha)

                val resultado = HttpConnection.post(url, map)

                uiThread {
                    val retorno = JSONObject(resultado)
                    val login_state = retorno.getBoolean("sucesso")
                    if (login_state == true){
                        val usuario = retorno.getJSONObject("usuario")
                        preferences.edit().putString("USUARIO", usuario.toString()).apply()
                        preferences.edit().putBoolean("STATUS", true).apply()
                        toast("TA LOGADO OTARIO")
                        finish()
                    }
                }
            }
        }

        txt_cadastro.setOnClickListener {
            finish()
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
}
