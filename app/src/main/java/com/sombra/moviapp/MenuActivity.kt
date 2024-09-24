package com.sombra.moviapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MenuActivity : AppCompatActivity() {

    // Declaración de FirebaseAuth
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        // Inicializa FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Referencias a los componentes del layout
        val userIdTextView = findViewById<TextView>(R.id.userIdTextView)
        val pedidosButton = findViewById<Button>(R.id.pedidosButton)
        val miCuentaButton = findViewById<Button>(R.id.miCuentaButton)

        // Muestra el ID del usuario
        val userId = auth.currentUser?.uid ?: "Invitado"
        userIdTextView.text = "Usuario: $userId"

        // Acción al hacer clic en el botón de Pedidos
        pedidosButton.setOnClickListener {
            // Redirigir a la actividad de pedidos (implementa la lógica de navegación)
            // val intent = Intent(this, PedidosActivity::class.java)
            // startActivity(intent)
        }

        // Acción al hacer clic en el botón de Mi Cuenta
        miCuentaButton.setOnClickListener {
            // Redirigir a la actividad de cuenta (implementa la lógica de navegación)
            // val intent = Intent(this, MiCuentaActivity::class.java)
            // startActivity(intent)
        }
    }
}
