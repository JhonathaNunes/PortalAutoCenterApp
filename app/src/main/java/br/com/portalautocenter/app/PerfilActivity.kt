package br.com.portalautocenter.app

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import br.com.portalautocenter.adapters.VeiculosAdapter
import br.com.portalautocenter.models.Produto
import br.com.portalautocenter.models.Veiculo
import br.com.portalautocenter.utils.HttpConnection
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_perfil.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONArray
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

        doAsync {
            val listVeiculos = ArrayList<Veiculo>()
            val jsonReturn = HttpConnection.get("http://10.0.2.2/inf4m/PortalAutoCenter/TCCPortalAutoCenter/api/veiculos/selecionar.php?idUsuario=" + usuario.getInt("idUsuario"))

            Log.d("TAG", jsonReturn)

            try {
                val jsonArray: JSONArray = JSONArray(jsonReturn)

                for (i in 0..jsonArray.length() step 1) run {
                    val v = Veiculo(jsonArray.getJSONObject(i).getInt("idVeiculo"), jsonArray.getJSONObject(i).getString("placa"), jsonArray.getJSONObject(i).getString("ano"),
                            jsonArray.getJSONObject(i).getString("cor"), jsonArray.getJSONObject(i).getInt("idModelo"), jsonArray.getJSONObject(i).getInt("idUsuario"),
                            jsonArray.getJSONObject(i).getString("acessorios"), jsonArray.getJSONObject(i).getString("modelo"), jsonArray.getJSONObject(i).getString("marca"),
                            jsonArray.getJSONObject(i).getString("foto"))

                    listVeiculos.add(v)

                }
            }catch (e:Exception){
                Log.e("Cometeu um erro: ", e.message)
            }

            uiThread {
                val customAdapter = VeiculosAdapter(applicationContext, listVeiculos)
                view_veiculos.adapter = customAdapter
            }
        }
    }
}
