package br.com.portalautocenter.models

/**
 * Created by 16254855 on 21/05/2018.
 */
class Estado (val id:Int, var sigla:String) {
    override fun toString(): String {
        return sigla
    }
}