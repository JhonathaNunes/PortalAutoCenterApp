package br.com.portalautocenter.app

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import br.com.portalautocenter.utils.InputMask
import br.com.portalautocenter.utils.Senha
import kotlinx.android.synthetic.main.activity_cadastro_usuario.*

class CadastroUsuarioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_usuario)

        //Insere a máscara de CPF
        txt_cpf.addTextChangedListener(InputMask.mask(txt_cpf, InputMask.FORMAT_CPF))

        btn_cadastro.setOnClickListener {
            val senha = txt_senha.text.toString()
            val s = Senha()

            if (senha.isEmpty()){
                txt_senha.setError(getString(R.string.erroVazio))
            }else if (s.validaSenha(senha)){

            }else{
                txt_senha.setError(getString(R.string.formatoSenhaIncorreto))
            }
        }
    }
}
