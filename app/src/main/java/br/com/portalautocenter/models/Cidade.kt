package br.com.portalautocenter.models

/**
 * Created by 16254855 on 21/05/2018.
 */
class Cidade (val id :Int, val nome:String, val idEstado: Int) {
    override fun toString(): String {
        return nome
    }
}