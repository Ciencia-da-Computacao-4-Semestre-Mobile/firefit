package com.example.myapplication

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar

class RegisterActivity : AppCompatActivity() {

    private lateinit var edtNome: EditText
    private lateinit var edtNascimento: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtSenha: EditText
    private lateinit var btnRegistrar: Button
    private lateinit var btnEntrar: Button
    private lateinit var txtPossuiConta: TextView

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register) // Certifique-se de que este é o nome correto do seu XML

        auth = FirebaseAuth.getInstance()

        // Referências aos elementos
        edtNome = findViewById(R.id.edtNome)
        edtNascimento = findViewById(R.id.edtNascimento)
        edtEmail = findViewById(R.id.edtEmail)
        edtSenha = findViewById(R.id.edtSenha)
        btnRegistrar = findViewById(R.id.btnRegistrar)
        btnEntrar = findViewById(R.id.btnEntrar)
        txtPossuiConta = findViewById(R.id.txtPossuiConta)

        // ---------- MÁSCARA DE DATA ----------
        edtNascimento.addTextChangedListener(object : TextWatcher {

            private var isUpdating = false
            private val mask = "##/##/####"

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isUpdating) return

                val str = s.toString().replace("/", "")
                var formatted = ""
                var i = 0

                for (c in mask.toCharArray()) {
                    if (c != '#' && i < str.length) {
                        formatted += c
                    } else {
                        if (i < str.length) {
                            formatted += str[i]
                            i++
                        } else {
                            break
                        }
                    }
                }

                isUpdating = true
                edtNascimento.setText(formatted)
                edtNascimento.setSelection(formatted.length)
                isUpdating = false
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // ---------- DATE PICKER AO CLICAR ----------
        edtNascimento.setOnClickListener {
            val c = Calendar.getInstance()
            val ano = c.get(Calendar.YEAR)
            val mes = c.get(Calendar.MONTH)
            val dia = c.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this, { _, y, m, d ->
                val dataFormatada = "%02d/%02d/%04d".format(d, m + 1, y)
                edtNascimento.setText(dataFormatada)
            }, ano, mes, dia)

            datePicker.show()
        }

        // ---------- BOTÃO REGISTRAR ----------
        btnRegistrar.setOnClickListener {
            val email = edtEmail.text.toString().trim()
            val senha = edtSenha.text.toString().trim()

            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        val nome = edtNome.text.toString()

                        val profileUpdates = com.google.firebase.auth.UserProfileChangeRequest.Builder()
                            .setDisplayName(nome)
                            .build()

                        auth.currentUser?.updateProfile(profileUpdates)

                        Toast.makeText(this, "Registrado com sucesso!", Toast.LENGTH_SHORT).show()

                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()

                    } else {
                        Toast.makeText(
                            this,
                            "Erro ao registrar: ${task.exception?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }

        // ---------- BOTÃO ENTRAR ----------
        btnEntrar.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // ---------- TOCAR NO TEXTO "JÁ POSSUI CONTA?" ----------
        txtPossuiConta.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
