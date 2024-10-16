package com.sombra.moviapp

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.sombra.moviapp.MiCuentaActivity
import com.sombra.moviapp.R
import com.sombra.moviapp.VerPedidosActivity
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MenuActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var map: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        auth = FirebaseAuth.getInstance()

        val userIdTextView = findViewById<TextView>(R.id.userIdTextView)
        val pedidosButton = findViewById<Button>(R.id.pedidosButton)
        val miCuentaButton = findViewById<Button>(R.id.miCuentaButton)

        val userId = auth.currentUser?.uid ?: "Invitado"
        userIdTextView.text = "Usuario: $userId"

        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))

        map = findViewById(R.id.map)
        setupMap()

        // Recibir datos del pedido
        val direccion = intent.getParcelableExtra<GeoPoint>("direccion")
        val totalCompra = intent.getDoubleExtra("totalCompra", 0.0)

        // Calcular costo de envío
        val costoEnvio = calcularCostoEnvio(totalCompra)

        // Agregar marcador en la dirección del pedido
        direccion?.let {
            mostrarDireccionEnMapa(it)
            mostrarCostoEnvio(costoEnvio)
        }

        pedidosButton.setOnClickListener {
            startActivity(Intent(this, VerPedidosActivity::class.java))
        }

        miCuentaButton.setOnClickListener {
            startActivity(Intent(this, MiCuentaActivity::class.java))
        }
    }

    private fun setupMap() {
        try {
            map.apply {
                setBuiltInZoomControls(true)
                setMultiTouchControls(true)

                controller.setZoom(15.0)
                val startPoint = GeoPoint(-33.4489, -70.6693)
                controller.setCenter(startPoint)

                val startMarker = Marker(this).apply {
                    position = startPoint
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    title = "Mi ubicación"
                }
                overlays.add(startMarker)
            }
        } catch (e: Exception) {
            Log.e("MenuActivity", "Error al inicializar el MapView: ${e.message}")
        }
    }

    private fun mostrarDireccionEnMapa(direccion: GeoPoint) {
        val direccionMarker = Marker(map).apply {
            position = direccion
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            title = "Dirección del pedido"
        }
        map.overlays.add(direccionMarker)
        map.controller.setCenter(direccion) // Centra el mapa en la dirección del pedido
    }

    private fun mostrarCostoEnvio(costo: Double) {
        Toast.makeText(this, "Costo de envío: $$costo", Toast.LENGTH_SHORT).show()
    }

    private fun calcularCostoEnvio(totalCompra: Double): Double {
        return when {
            totalCompra > 50000 -> 0.0 // Envío gratuito
            totalCompra in 25000.0..49999.0 -> 150.0 // Tarifa por kilómetro
            else -> 300.0 // Tarifa básica
        }
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }
}
