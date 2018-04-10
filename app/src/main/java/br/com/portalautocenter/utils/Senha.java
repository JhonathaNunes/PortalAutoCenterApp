package br.com.portalautocenter.utils;

/**
 * Created by 16254855 on 10/04/2018.
 */

public class Senha {
    /*Verifica se a senha possui o formato exigido*/
    public boolean validaSenha(String senha){
        Boolean valido = false;
        Boolean maiusculo = false;
        Boolean numero = false;

        for (int i = 0; i < senha.length(); i++){
            if (Character.isDigit(senha.charAt(i))){
                numero = true;
                break;
            }
        }

        for (int i = 0; i < senha.length(); i++){
            if (Character.isUpperCase(senha.charAt(i))){
                maiusculo = true;
                break;
            }
        }

        if (senha.length() >= 8 && numero && maiusculo){
            valido = true;
        }

        return valido;
    }
}
