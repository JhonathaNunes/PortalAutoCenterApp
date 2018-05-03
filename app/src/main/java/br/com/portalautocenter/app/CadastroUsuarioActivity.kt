package br.com.portalautocenter.app

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import br.com.portalautocenter.utils.HttpConnection
import br.com.portalautocenter.utils.InputMask
import br.com.portalautocenter.utils.Senha
import kotlinx.android.synthetic.main.activity_cadastro_usuario.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import org.json.JSONObject

class CadastroUsuarioActivity : AppCompatActivity() {
    var senha:String = ""
    var s = Senha()

    var nome:String = ""
    var cpf:String = ""
    var email:String = ""
    var usuario:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_usuario)

        //Insere a máscara de CPF
        txt_cpf.addTextChangedListener(InputMask.mask(txt_cpf, InputMask.FORMAT_CPF))

        btn_cadastro.setOnClickListener {
            if (validaCampos()){
                doAsync {
                    val url ="http://10.0.2.2/inf4m/PortalAutoCenter/TCCPortalAutoCenter/api/usuario/inserir.php"
                    val map:HashMap<String, String> = hashMapOf("nome" to nome, "cpf" to cpf, "email" to email, "usuario" to usuario, "senha" to senha)
                    val resultado = HttpConnection.post(url, map)
                    val resultadoJson = JSONObject(resultado)
                    val sucesso = resultadoJson.getBoolean("sucesso")
                    uiThread {
                        if (sucesso){
                            toast("Usuário cadastrado com sucesso!")
                            finish()
                        }else{
                            toast("Falha no cadastro, tente novamente mais tarde!")
                        }
                    }
                }
            }
        }
    }

    fun validaCampos():Boolean {
        var valido = true
        senha = txt_senha.text.toString()

        if (txt_nome.text.toString().isEmpty()){
            txt_nome.setError("Digite o seu nome")
            nome = txt_nome.text.toString()
        }

        if (txt_cpf.text.toString().isEmpty() or (txt_cpf.text.toString().length < 14)){
            txt_cpf.setError("Digite o CPF")
            cpf = txt_cpf.text.toString()
        }

        if (txt_email.text.toString().isEmpty()){
            txt_email.setError("Digite o e-mail")
            email = txt_email.text.toString()
        }

        if (txt_usuario.text.toString().isEmpty()){
            txt_usuario.setError(" Digite um nome de usuário")
            usuario = txt_usuario.text.toString()
        }

        if (senha.isEmpty()){
            txt_senha.setError(getString(R.string.erroVazio))
        }else if (s.validaSenha(senha) != true){
            txt_senha.setError(getString(R.string.formatoSenhaIncorreto))
        }
        return valido
    }
}
