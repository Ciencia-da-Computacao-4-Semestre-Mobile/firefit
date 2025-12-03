package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class UserActivity : AppCompatActivity() {

    private lateinit var txtUserName: TextView
    private lateinit var txtImcValor: TextView
    private lateinit var txtImcStatus: TextView
    private lateinit var imgUser: ImageView
    private lateinit var txtAlterarImg: TextView

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private val PICK_IMAGE = 200
    private val PERSONAL_DATA_REQUEST = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        txtUserName = findViewById(R.id.txtUserName)
        imgUser = findViewById(R.id.imgUser)
        txtAlterarImg = findViewById(R.id.txtAlterarImg)
        txtImcValor = findViewById(R.id.txtImcValor)
        txtImcStatus = findViewById(R.id.txtImcStatus)

        carregarFoto()
        carregarNomeDoAuth()
        carregarImcDoFirestore()

        txtAlterarImg.setOnClickListener { escolherFotoGaleria() }

        setupBottomNav()

        findViewById<TextView>(R.id.txtDesconectar).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        findViewById<LinearLayout>(R.id.cardPersonalData).setOnClickListener {
            val intent = Intent(this, PersonalDataActivity::class.java)
            startActivityForResult(intent, PERSONAL_DATA_REQUEST)
        }
    }

    private fun escolherFotoGaleria() {
        val intent = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
        startActivityForResult(intent, PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            PICK_IMAGE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val imageUri = data?.data ?: return
                    uploadFotoFirebase(imageUri)
                }
            }

            PERSONAL_DATA_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    // atualiza UI com extras (se existirem)
                    data?.let { intent ->
                        val name = intent.getStringExtra("name")
                        if (!name.isNullOrEmpty()) txtUserName.text = name

                        // extras podem ser enviados como Double (putExtra Double) — usamos getDoubleExtra com fallback
                        val peso = intent.getDoubleExtra("peso", 0.0)
                        val altura = intent.getDoubleExtra("altura", 0.0)

                        if (peso > 0.0 && altura > 0.0) {
                            val alturaM = altura / 100.0
                            val imc = peso / (alturaM * alturaM)
                            txtImcValor.text = String.format("%.1f", imc)
                            txtImcStatus.text = when {
                                imc < 18.5 -> "Abaixo do peso"
                                imc < 24.9 -> "Normal"
                                imc < 29.9 -> "Sobrepeso"
                                else -> "Obesidade"
                            }
                        } else {
                            // fallback: recarrega do Firestore
                            carregarImcDoFirestore()
                        }
                    } ?: run {
                        carregarNomeDoAuth()
                        carregarImcDoFirestore()
                    }

                    // garante que o nome do Auth seja atualizado na UI
                    carregarNomeDoAuth()
                }
            }
        }
    }

    private fun uploadFotoFirebase(imageUri: Uri) {
        val uid = auth.currentUser?.uid ?: return
        val storageRef = FirebaseStorage.getInstance().getReference("profilePhotos/$uid.jpg")

        storageRef.putFile(imageUri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->

                    // Atualiza o Auth
                    val updates = UserProfileChangeRequest.Builder()
                        .setPhotoUri(downloadUrl)
                        .build()
                    auth.currentUser?.updateProfile(updates)

                    // Atualiza o Firestore
                    firestore.collection("PersonalData").document(uid)
                        .update("photoUrl", downloadUrl.toString())
                        .addOnSuccessListener {
                            carregarFoto() // recarrega imagem na hora
                        }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao enviar foto", Toast.LENGTH_SHORT).show()
            }
    }


    private fun carregarFoto() {
        val uid = auth.currentUser?.uid ?: return

        firestore.collection("PersonalData").document(uid)
            .get()
            .addOnSuccessListener { doc ->
                val fotoUrl = doc.getString("photoUrl")

                Glide.with(this)
                    .load(fotoUrl)
                    .placeholder(R.drawable.ic_user_placeholder)
                    .into(imgUser)
            }
            .addOnFailureListener {
                Glide.with(this)
                    .load(R.drawable.ic_user_placeholder)
                    .into(imgUser)
            }
    }


    private fun carregarNomeDoAuth() {
        txtUserName.text = auth.currentUser?.displayName ?: "Usuário"
    }

    private fun carregarImcDoFirestore() {
        val uid = auth.currentUser?.uid ?: return

        firestore.collection("PersonalData").document(uid)
            .get()
            .addOnSuccessListener { doc ->
                val peso = (doc.get("peso") as? Number)?.toDouble()
                    ?: (doc.get("peso") as? String)?.toDoubleOrNull()
                    ?: 0.0

                val altura = (doc.get("altura") as? Number)?.toDouble()
                    ?: (doc.get("altura") as? String)?.toDoubleOrNull()
                    ?: 0.0

                if (peso <= 0.0 || altura <= 0.0) {
                    txtImcValor.text = "--"
                    txtImcStatus.text = "Sem dados"
                    return@addOnSuccessListener
                }

                val alturaM = altura / 100.0
                val imc = peso / (alturaM * alturaM)
                txtImcValor.text = String.format("%.1f", imc)
                txtImcStatus.text = when {
                    imc < 18.5 -> "Abaixo do peso"
                    imc < 24.9 -> "Normal"
                    imc < 29.9 -> "Sobrepeso"
                    else -> "Obesidade"
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao carregar IMC: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setupBottomNav() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.selectedItemId = R.id.nav_user

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                R.id.nav_training -> {
                    startActivity(Intent(this, WorkoutsActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                R.id.nav_user -> true
                R.id.nav_events -> {
                    startActivity(Intent(this, ScheduledEventsActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}
