package com.example.myapplication

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class PersonalDataActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

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
        val user = auth.currentUser

        // ==============================
        // FINDVIEWBYS
        // ==============================

        btnBack = findViewById(R.id.btnBack)
        btnSalvar = findViewById(R.id.btnSalvar)

        inputNome = findViewById(R.id.inputNome)
        inputEmail = findViewById(R.id.inputEmail)
        inputNascimento = findViewById(R.id.inputNascimento)
        inputTelefone = findViewById(R.id.inputTelefone)
        inputIdade = findViewById(R.id.inputIdade)
        inputPeso = findViewById(R.id.inputPeso)
        inputAltura = findViewById(R.id.inputAltura)

        // ==============================
        // PREENCHE CAMPOS EXISTENTES
        // ==============================

        inputNome.setText(user?.displayName ?: "")
        inputEmail.setText(user?.email ?: "")

        carregarDadosExtras()

        // ==============================
        // BOTÃO VOLTAR
        // ==============================
        btnBack.setOnClickListener {
            finish()
        }

        // ==============================
        // BOTÃO SALVAR
        // ==============================
        btnSalvar.setOnClickListener {
            salvarDados()
        }
    }

    // ==============================
    // CARREGA DADOS DO FIREBASE
    // ==============================
    private fun carregarDadosExtras() {
        val uid = auth.currentUser?.uid ?: return
        val db = FirebaseDatabase.getInstance().getReference("users").child(uid)

        db.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {

                inputNascimento.setText(snapshot.child("nascimento").value?.toString() ?: "")
                inputTelefone.setText(snapshot.child("telefone").value?.toString() ?: "")
                inputIdade.setText(snapshot.child("idade").value?.toString() ?: "")
                inputPeso.setText(snapshot.child("peso").value?.toString() ?: "")
                inputAltura.setText(snapshot.child("altura").value?.toString() ?: "")
            }
        }
    }

    // ==============================
    // SALVA NO FIREBASE
    // ==============================
    private fun salvarDados() {

        val user = auth.currentUser ?: return

        val nome = inputNome.text.toString()
        val nascimento = inputNascimento.text.toString()
        val telefone = inputTelefone.text.toString()
        val idade = inputIdade.text.toString()
        val peso = inputPeso.text.toString()
        val altura = inputAltura.text.toString()

        // Atualiza nome no Firebase Auth
        val profileUpdate = com.google.firebase.auth.UserProfileChangeRequest.Builder()
            .setDisplayName(nome)
            .build()

        user.updateProfile(profileUpdate)

        // Atualiza dados extras no Realtime Database
        val uid = user.uid
        val db = FirebaseDatabase.getInstance().getReference("users").child(uid)

        val dados = mapOf(
            "nascimento" to nascimento,
            "telefone" to telefone,
            "idade" to idade,
            "peso" to peso,
            "altura" to altura
        )

        db.updateChildren(dados).addOnCompleteListener {
            Toast.makeText(this, "Dados atualizados!", Toast.LENGTH_SHORT).show()
        }
    }
}
