package com.sombra.moviapp

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import kotlin.random.Random

class AsignarDireccionActivity : AppCompatActivity() {

    private lateinit var asignarButton: Button
    private lateinit var firestore: FirebaseFirestore
    private lateinit var pedidoId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asignar_direccion)

        // Inicializa Firestore
        firestore = FirebaseFirestore.getInstance()

        // Obtiene el ID del pedido
        pedidoId = intent.getStringExtra("pedidoId") ?: ""

        // Asigna una dirección aleatoria al cargar la actividad
        val direccion = generarDireccionAleatoria()
        asignarDireccion(pedidoId, direccion)

        // Deshabilita el botón, ya que no es necesario
        asignarButton.isEnabled = false
    }

    private fun generarDireccionAleatoria(): GeoPoint {
        // Genera coordenadas aleatorias dentro de un rango específico de Santiago, Chile
        val latitud = -33.4489 + Random.nextDouble(-0.1, 0.1) // Rango de latitud
        val longitud = -70.6693 + Random.nextDouble(-0.1, 0.1) // Rango de longitud
        return GeoPoint(latitud, longitud)
    }

    private fun asignarDireccion(pedidoId: String, direccion: GeoPoint) {
        // Actualiza el pedido en Firestore con la dirección GeoPoint
        firestore.collection("pedidos").document(pedidoId)
            .update("direccion", direccion)
            .addOnSuccessListener {
                Toast.makeText(this, "Dirección asignada automáticamente.", Toast.LENGTH_SHORT).show()
                finish() // Cierra la actividad
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al asignar dirección: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
