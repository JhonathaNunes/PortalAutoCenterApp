package br.com.portalautocenter.app

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import br.com.portalautocenter.models.Produto
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_perfil.*
import org.json.JSONObject

class PerfilActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)

        val usuario = getSharedPreferences("LOGADO", Context.MODE_PRIVATE)

        if (usuario.getBoolean("STATUS", false)){
            preencheCampos(usuario.getString("USUARIO", "NoUser"))
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

    fun preencheCampos(json:String){
        val usuario = JSONObject(json)

        txt_nome.setText(usuario.getString("nome"))
        txt_cpf.setText(usuario.getString("cpf"))
        txt_email.setText(usuario.getString("email"))
        txt_usuario.setText(usuario.getString("usuario"))
        val url = "http://10.0.2.2/inf4m/PortalAutoCenter/TCCPortalAutoCenter/"+usuario.getString("fotoUser")
        Picasso.with(applicationContext).load(url).into(profile_image)
    }
}
