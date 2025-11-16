package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // BOTÃO REGISTRAR
        binding.btnRegistrar.setOnClickListener {
            val nome = binding.edtNome.text.toString()
            val dataNasc = binding.edtNascimento.text.toString()
            val email = binding.edtEmail.text.toString()
            val senha = binding.edtSenha.text.toString()

            // Aqui você adiciona a lógica de cadastro
        }


        // BOTÃO ENTRAR (laranja grande no final)
        binding.btnEntrar.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // impede voltar para o registro
        }
    }
}
