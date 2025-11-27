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
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var imgUser: ImageView
    private lateinit var txtAlterarImg: TextView
    private val PICK_IMAGE = 200
    private val PERSONAL_DATA_REQUEST = 100

    private lateinit var txtImcValor: TextView
    private lateinit var txtImcStatus: TextView

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
        carregarImcDoFirestore()

        txtAlterarImg.setOnClickListener { escolherFotoGaleria() }

        txtUserName.text = auth.currentUser?.displayName ?: "Usuário"

        // BottomNav
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
                else -> false
            }
        }

        findViewById<TextView>(R.id.txtDesconectar).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        // Abrir PersonalDataActivity com startActivityForResult
        findViewById<LinearLayout>(R.id.cardPersonalData).setOnClickListener {
            val intent = Intent(this, PersonalDataActivity::class.java)
            startActivityForResult(intent, PERSONAL_DATA_REQUEST)
        }

        findViewById<LinearLayout>(R.id.cardWorkouts).setOnClickListener {
            startActivity(Intent(this, WorkoutsSavedActivity::class.java))
        }
    }

    // Escolher foto
    private fun escolherFotoGaleria() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val uid = auth.currentUser?.uid ?: return

        when (requestCode) {
            PICK_IMAGE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val imageUri = data?.data ?: return
                    imgUser.setImageURI(imageUri)
                    uploadFotoFirebase(imageUri)
                }
            }
            PERSONAL_DATA_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    // Recarrega nome, peso, altura e IMC
                    txtUserName.text = auth.currentUser?.displayName ?: "Usuário"
                    carregarImcDoFirestore()
                }
            }
        }
    }

    // Upload foto
    private fun uploadFotoFirebase(imageUri: Uri) {
        val uid = auth.currentUser?.uid ?: return

        val storageRef = FirebaseStorage.getInstance().getReference("profilePhotos/$uid.jpg")

        storageRef.putFile(imageUri)
            .addOnSuccessListener {

                storageRef.downloadUrl.addOnSuccessListener { url ->

                    val updates = UserProfileChangeRequest.Builder()
                        .setPhotoUri(url)
                        .build()

                    auth.currentUser?.updateProfile(updates)

                    firestore.collection("PersonalData")
                        .document(uid)
                        .update("photoUrl", url.toString())

                    Toast.makeText(this, "Foto atualizada!", Toast.LENGTH_SHORT).show()

                    carregarFoto()
                }
            }
    }

    private fun carregarFoto() {
        val foto = auth.currentUser?.photoUrl
        Glide.with(this)
            .load(foto)
            .placeholder(R.drawable.ic_user_placeholder)
            .into(imgUser)
    }

    // IMC via Firestore
    private fun carregarImcDoFirestore() {
        val uid = auth.currentUser?.uid ?: return

        firestore.collection("PersonalData")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->

                val peso = doc.get("peso")?.toString()?.toDoubleOrNull() ?: 0.0
                val alturaCm = doc.get("altura")?.toString()?.toDoubleOrNull() ?: 0.0

                if (peso == 0.0 || alturaCm == 0.0) {
                    txtImcValor.text = "--"
                    txtImcStatus.text = "Sem dados"
                    return@addOnSuccessListener
                }

                val altura = alturaCm / 100
                val imc = peso / (altura * altura)

                val status = when {
                    imc < 18.5 -> "Abaixo do peso"
                    imc < 24.9 -> "Normal"
                    imc < 29.9 -> "Sobrepeso"
                    else -> "Obesidade"
                }

                txtImcValor.text = String.format("%.1f", imc)
                txtImcStatus.text = status
            }
    }
}
