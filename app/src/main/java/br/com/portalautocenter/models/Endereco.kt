package br.com.portalautocenter.models

/**
 * Created by 16254855 on 15/05/2018.
 */
class Endereco(val idEnderecoUsuario:Int,
               val logradouro:String,
               val numero:String,
               val complemento:String,
               val bairro:String,
               val cep:String,
               val idTipoEndereco:Int,
               val idCidade:Int,
               val idUsuario:Int,
               val descricao:String
) {
}