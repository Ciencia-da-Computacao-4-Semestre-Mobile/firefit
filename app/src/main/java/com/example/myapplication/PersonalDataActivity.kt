package com.example.myapplication

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PersonalDataActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var btnBack: ImageView
    private lateinit var btnSalvar: Button

    private lateinit var inputNome: EditText
    private lateinit var inputEmail: EditText
    private lateinit var inputNascimento: EditText
    private lateinit var inputTelefone: EditText
    private lateinit var inputIdade: EditText
    private lateinit var inputPeso: EditText
    private lateinit var inputAltura: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_data)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        val user = auth.currentUser

        // FINDVIEWBYS
        btnBack = findViewById(R.id.btnBack)
        btnSalvar = findViewById(R.id.btnSalvar)

        inputNome = findViewById(R.id.inputNome)
        inputEmail = findViewById(R.id.inputEmail)
        inputNascimento = findViewById(R.id.inputNascimento)
        inputTelefone = findViewById(R.id.inputTelefone)
        inputIdade = findViewById(R.id.inputIdade)
        inputPeso = findViewById(R.id.inputPeso)
        inputAltura = findViewById(R.id.inputAltura)

        // PREENCHE CAMPOS EXISTENTES
        inputNome.setText(user?.displayName ?: "")
        inputEmail.setText(user?.email ?: "")

        carregarDadosExtras()

        // BOTÃO VOLTAR
        btnBack.setOnClickListener {
            finish()
        }

        // BOTÃO SALVAR
        btnSalvar.setOnClickListener {
            salvarFirestore()
        }
    }

    private fun carregarDadosExtras() {
        val uid = auth.currentUser?.uid ?: return
        val userRef = db.collection("users").document(uid)

        userRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                inputNascimento.setText(document.getString("nascimento") ?: "")
                inputTelefone.setText(document.getString("telefone") ?: "")
                inputIdade.setText(document.getLong("idade")?.toString() ?: "")
                inputPeso.setText(document.getDouble("peso")?.toString() ?: "")
                inputAltura.setText(document.getDouble("altura")?.toString() ?: "")
            }
        }
    }

    private fun salvarFirestore() {
        val user = auth.currentUser ?: return

        val nome = inputNome.text.toString()
        val nascimento = inputNascimento.text.toString()
        val telefone = inputTelefone.text.toString()
        val idade = inputIdade.text.toString().toIntOrNull() ?: 0
        val peso = inputPeso.text.toString().toDoubleOrNull() ?: 0.0
        val altura = inputAltura.text.toString().toDoubleOrNull() ?: 0.0

        // Atualiza nome no FirebaseAuth
        val profileUpdate = com.google.firebase.auth.UserProfileChangeRequest.Builder()
            .setDisplayName(nome)
            .build()

        user.updateProfile(profileUpdate).addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Toast.makeText(this, "Erro ao atualizar nome!", Toast.LENGTH_SHORT).show()
                return@addOnCompleteListener
            }

            // Atualiza Firestore
            val uid = user.uid
            val userRef = db.collection("users").document(uid)

            val dados = mapOf(
                "nascimento" to nascimento,
                "telefone" to telefone,
                "idade" to idade,
                "peso" to peso,
                "altura" to altura
            )

            userRef.set(dados, com.google.firebase.firestore.SetOptions.merge())
                .addOnSuccessListener {
                    Toast.makeText(this, "Dados atualizados com sucesso!", Toast.LENGTH_SHORT).show()

                    // Aqui é o ponto chave: fecha PersonalDataActivity e volta para UserActivity
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Erro ao atualizar Firestore!", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
