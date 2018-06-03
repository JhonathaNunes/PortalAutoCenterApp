package br.com.portalautocenter.models

import java.io.Serializable

/**
 * Created by 16254855 on 04/04/2018.
 */
class Produto(var id:Int, var nome:String, var preco:Double, var descricao:String, var idFilial:Int, var imagem:String,
              var marca:String, var fabricante:String, var obs:String, var garantia:String) : Serializable{
}