package br.com.portalautocenter.app

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
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
                        val nome = usuario.getString("nome")
                        toast("Usuario $nome logado com sucesso")
                        finish()
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home
            -> {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            else -> {
            }
        }
        return true
    }
}
