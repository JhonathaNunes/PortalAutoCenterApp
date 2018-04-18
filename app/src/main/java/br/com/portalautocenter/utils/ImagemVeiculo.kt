package br.com.portalautocenter.utils

import android.util.Log
import br.com.portalautocenter.models.Veiculo
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONArray

/**
 * Created by 16254855 on 18/04/2018.
 */
fun pegarImagem(idVeiculo: Int):String{
        val listVeiculos = ArrayList<Veiculo>()
        val jsonReturn = HttpConnection.get("http://10.0.2.2/inf4m/PortalAutoCenter/TCCPortalAutoCenter/api/veiculos/selecionarImagens.php?idVeiculo=" +
                "$idVeiculo")

        Log.d("TAG", jsonReturn)

    var imagem = "null"

        try {
            val jsonArray: JSONArray = JSONArray(jsonReturn)

            imagem = jsonArray.getJSONObject(0).getString("foto")
        }catch (e:Exception){
            Log.e("Cometeu um erro: ", e.message)
        }

    return imagem
}