package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.myapplication.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth   // IMPORTANTE!!
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth   // DECLARAÇÃO DO AUTH

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // INICIALIZA O FIREBASE AUTH
        auth = FirebaseAuth.getInstance()

        // BOTÃO ENTRAR
        binding.btnEntrar.setOnClickListener {
            val email = binding.edtEmail.text.toString().trim()
            val senha = binding.edtSenha.text.toString().trim()

            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // LOGIN FIREBASE
            auth.signInWithEmailAndPassword(email, senha)
                .addOnSuccessListener {

                    Toast.makeText(this, "Login realizado!", Toast.LENGTH_SHORT).show()


                    val uid = auth.currentUser!!.uid
                    val db = FirebaseFirestore.getInstance()

                    db.collection("usuarios").document(uid).get()
                        .addOnSuccessListener { document ->
                            if (document.exists()) {
                                val nome = document.getString("nome") ?: "Usuario"

                                val prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                                prefs.edit().putString("username", nome).apply()
                            }
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Erro ao buscar nome do usuário", Toast.LENGTH_SHORT).show()
                        }


                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Erro: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }

        // BOTÃO REGISTRAR
        binding.btnRegistrar.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // ESQUECI MINHA SENHA
        binding.txtForgot.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
}
