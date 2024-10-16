package com.sombra.moviapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MiCuentaActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mi_cuenta)

        // Inicializa FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Referencias a los componentes del layout
        val userIdTextView = findViewById<TextView>(R.id.userIdTextView)
        val creationDateTextView = findViewById<TextView>(R.id.creationDateTextView)
        val logoutButton = findViewById<Button>(R.id.logoutButton)

        // Obtener el ID del usuario
        val userId = auth.currentUser?.uid ?: "Invitado"
        userIdTextView.text = "Usuario: $userId"

        // Obtener la fecha de creación de la cuenta
        val creationDate = auth.currentUser?.metadata?.creationTimestamp ?: 0
        val date = Date(creationDate)
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        creationDateTextView.text = "Fecha de creación: ${sdf.format(date)}"

        // Acción al hacer clic en el botón de Cerrar sesión
        logoutButton.setOnClickListener {
            auth.signOut()
            // Redirigir a la actividad principal o de inicio de sesión
            finish() // Cierra esta actividad
        }
    }
}
