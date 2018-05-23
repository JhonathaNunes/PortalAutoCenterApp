package br.com.portalautocenter.utils

import java.text.DateFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by 16254855 on 18/04/2018.
 */
fun converteParaReal(double:Double):String{
    //Converte para real
    val brasil = Locale("pt", "BR")
    val nf = NumberFormat.getCurrencyInstance(brasil)

    val preco = nf.format(double)

    return preco
}

fun converteData(data:String):String{
    var format = SimpleDateFormat("yyyy-MM-dd")
    var dataParse = format.parse(data)

    format = SimpleDateFormat("dd/MM/yyyy")

    var dataFormatada = format.format(dataParse)

    return dataFormatada
}