package com.sombra.moviapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {

    // Declaración de FirebaseAuth y FusedLocationProviderClient
    private lateinit var auth: FirebaseAuth
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializa FirebaseAuth y FusedLocationProviderClient
        auth = FirebaseAuth.getInstance()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Referencias a los componentes del layout
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val errorTextView = findViewById<TextView>(R.id.errorTextView)
        val registerButton = findViewById<Button>(R.id.registerButton) // Nuevo botón de registro

        // Acción al hacer clic en el botón de Login
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // Verifica si los campos están vacíos
            if (email.isEmpty() || password.isEmpty()) {
                errorTextView.text = "Email y contraseña no pueden estar vacíos"
                errorTextView.visibility = TextView.VISIBLE
                return@setOnClickListener
            }

            // Autenticar con Firebase
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Login exitoso, obtener ubicación y almacenarla
                        getLocationAndStoreInDatabase()
                    } else {
                        // Si falla el login
                        errorTextView.text = "Error: ${task.exception?.message}"
                        errorTextView.visibility = TextView.VISIBLE
                    }
                }
        }

        // Acción al hacer clic en el botón de Registro
        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    // Obtener la ubicación y almacenarla en Firebase
    private fun getLocationAndStoreInDatabase() {
        // Verificar permisos de ubicación
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Solicitar permisos de ubicación
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            // Obtener la última ubicación conocida
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        storeLocationInDatabase(location.latitude, location.longitude)
                    } else {
                        Snackbar.make(findViewById(android.R.id.content), "No se pudo obtener la ubicación.", Snackbar.LENGTH_LONG).show()
                    }
                }
                .addOnFailureListener {
                    Snackbar.make(findViewById(android.R.id.content), "Error al intentar obtener la ubicación.", Snackbar.LENGTH_LONG).show()
                }
        }
    }

    // Almacenar ubicación en Firebase
    private fun storeLocationInDatabase(latitude: Double, longitude: Double) {
        val userId = auth.currentUser?.uid
        val database = FirebaseDatabase.getInstance().getReference("GPS/$userId") // Ruta válida

        val locationData = hashMapOf(
            "latitude" to latitude,
            "longitude" to longitude
        )

        database.setValue(locationData).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Redirigir a MenuActivity después de almacenar la ubicación
                val intent = Intent(this, MenuActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Snackbar.make(findViewById(android.R.id.content), "Error al almacenar la ubicación.", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocationAndStoreInDatabase()
            } else {
                Snackbar.make(findViewById(android.R.id.content), "Permiso de ubicación denegado.", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}
