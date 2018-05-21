package br.com.portalautocenter.models

/**
 * Created by 16254855 on 21/05/2018.
 */
class TipoEndereco (val id : Int, val desc : String) {
    override fun toString(): String {
        return desc
    }
}