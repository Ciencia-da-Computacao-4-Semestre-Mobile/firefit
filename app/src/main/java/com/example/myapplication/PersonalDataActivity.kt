package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

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

    // Handler para fallback (evita "SALVANDO..." infinito)
    private val handler = Handler(Looper.getMainLooper())
    private var timeoutRunnable: Runnable? = null
    private val TIMEOUT_MS = 15000L // 15 segundos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_data)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

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
        inputNome.setText(user?.displayName ?: "")
        inputEmail.setText(user?.email ?: "")

        carregarDadosExtras()

        btnBack.setOnClickListener { finish() }
        btnSalvar.setOnClickListener { salvarFirestore() }
    }

    private fun carregarDadosExtras() {
        val uid = auth.currentUser?.uid ?: return

        db.collection("PersonalData").document(uid)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    inputNascimento.setText(document.getString("nascimento") ?: "")
                    inputTelefone.setText(document.getString("telefone") ?: "")

                    val idade = (document.get("idade") as? Number)?.toInt()
                        ?: (document.get("idade") as? String)?.toIntOrNull()

                    val peso = (document.get("peso") as? Number)?.toDouble()
                        ?: (document.get("peso") as? String)?.toDoubleOrNull()

                    val altura = (document.get("altura") as? Number)?.toDouble()
                        ?: (document.get("altura") as? String)?.toDoubleOrNull()

                    inputIdade.setText(idade?.toString() ?: "")
                    inputPeso.setText(peso?.toString() ?: "")
                    inputAltura.setText(altura?.toString() ?: "")
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao carregar dados: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun salvarFirestore() {
        val user = auth.currentUser ?: run {
            Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_SHORT).show()
            return
        }

        val nome = inputNome.text.toString().trim()
        val nascimento = inputNascimento.text.toString().trim()
        val telefone = inputTelefone.text.toString().trim()
        val idade = inputIdade.text.toString().toIntOrNull()
        val peso = inputPeso.text.toString().toDoubleOrNull()
        val altura = inputAltura.text.toString().toDoubleOrNull()

        if (nome.isEmpty()) {
            Toast.makeText(this, "Preencha o nome", Toast.LENGTH_SHORT).show()
            return
        }

        // Desativa botão e mostra estado
        setSavingState(true)

        // Inicia timeout fallback: garante que botão será reativado se algo congelar
        startTimeoutFallback()

        val profileUpdate = UserProfileChangeRequest.Builder()
            .setDisplayName(nome)
            .build()

        // Atualiza o perfil do FirebaseAuth
        user.updateProfile(profileUpdate)
            .addOnSuccessListener {
                // Preparar dados
                val dados = mutableMapOf<String, Any>(
                    "nascimento" to nascimento,
                    "telefone" to telefone
                )
                idade?.let { dados["idade"] = it }
                peso?.let { dados["peso"] = it }
                altura?.let { dados["altura"] = it }

                // Salva no Firestore
                db.collection("PersonalData").document(user.uid)
                    .set(dados, SetOptions.merge())
                    .addOnCompleteListener { task ->
                        // cancela fallback
                        cancelTimeoutFallback()

                        if (task.isSuccessful) {
                            // Prepara intent de resultado para voltar com dados já atualizados
                            val resultIntent = Intent().apply {
                                putExtra("name", nome)
                                idade?.let { putExtra("idade", it) }
                                peso?.let { putExtra("peso", it) }
                                altura?.let { putExtra("altura", it) }
                            }

                            Toast.makeText(this, "Dados atualizados com sucesso!", Toast.LENGTH_SHORT).show()
                            setSavingState(false) // não essencial, pois finish() abaixo, mas deixa consistente
                            setResult(Activity.RESULT_OK, resultIntent)
                            finish()
                        } else {
                            // Falha ao salvar no Firestore
                            setSavingState(false)
                            val ex = task.exception
                            Toast.makeText(this, "Erro ao salvar: ${ex?.message ?: "Desconhecido"}", Toast.LENGTH_LONG).show()
                        }
                    }
            }
            .addOnFailureListener { e ->
                // Falha ao atualizar nome no auth
                cancelTimeoutFallback()
                setSavingState(false)
                Toast.makeText(this, "Erro ao atualizar nome: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun setSavingState(isSaving: Boolean) {
        btnSalvar.isEnabled = !isSaving
        btnSalvar.text = if (isSaving) "SALVANDO..." else "SALVAR"
    }

    private fun startTimeoutFallback() {
        cancelTimeoutFallback()
        timeoutRunnable = Runnable {
            // fallback executado após TIMEOUT_MS
            setSavingState(false)
            Toast.makeText(this, "Tempo de salvamento esgotado. Verifique conexão / permissões.", Toast.LENGTH_LONG).show()
        }
        handler.postDelayed(timeoutRunnable!!, TIMEOUT_MS)
    }

    private fun cancelTimeoutFallback() {
        timeoutRunnable?.let { handler.removeCallbacks(it) }
        timeoutRunnable = null
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelTimeoutFallback()
    }
}
