package br.com.portalautocenter.models

import java.io.Serializable

/**
 * Created by 16254855 on 18/04/2018.
 */
class Veiculo(var idVeiculo:Int, var placa:String, var ano:String, var cor:String, var idModelo:Int,
              var idUsuario:Int, var acessorios:String, var modelo:String, var marca:String, var foto:String):Serializable {


}