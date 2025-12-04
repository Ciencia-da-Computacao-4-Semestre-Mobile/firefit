package com.example.myapplication

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class PersonalDataActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var dbFirestore: FirebaseFirestore

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
        dbFirestore = FirebaseFirestore.getInstance()

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

        val user = auth.currentUser

        // ==============================
        // PREENCHE CAMPOS EXISTENTES
        // ==============================
        inputNome.setText(user?.displayName ?: "")
        inputEmail.setText(user?.email ?: "")

        carregarDadosExtras()

        // BTN VOLTAR
        btnBack.setOnClickListener { finish() }

        // BTN SALVAR
        btnSalvar.setOnClickListener { salvarDados() }
    }

    // ==============================
    // CARREGA DADOS DO REALTIME DATABASE
    // ==============================
    private fun carregarDadosExtras() {
        val uid = auth.currentUser?.uid ?: return

        val ref = FirebaseDatabase.getInstance().getReference("users").child(uid)

        ref.get().addOnSuccessListener { snapshot ->
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
    // SALVA OS DADOS
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
        val profileUpdate = UserProfileChangeRequest.Builder()
            .setDisplayName(nome)
            .build()

        user.updateProfile(profileUpdate)

        // Atualiza dados extras no Realtime Database
        val uid = user.uid
        val ref = FirebaseDatabase.getInstance().getReference("users").child(uid)

        val dados = mapOf(
            "nascimento" to nascimento,
            "telefone" to telefone,
            "idade" to idade,
            "peso" to peso,
            "altura" to altura
        )

        ref.updateChildren(dados).addOnCompleteListener {
            Toast.makeText(this, "Dados atualizados!", Toast.LENGTH_SHORT).show()
        }
    }
}
