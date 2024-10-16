package com.sombra.moviapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class VerPedidosActivity : AppCompatActivity() {
    private lateinit var pedidosContainer: LinearLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_pedidos)

        firestore = FirebaseFirestore.getInstance()
        pedidosContainer = findViewById(R.id.pedidosContainer)
        progressBar = findViewById(R.id.progressBar)

        cargarPedidos()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun cargarPedidos() {
        progressBar.visibility = View.VISIBLE
        pedidosContainer.removeAllViews()

        firestore.collection("pedidos")
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Toast.makeText(this, "No hay pedidos disponibles.", Toast.LENGTH_SHORT).show()
                } else {
                    for (document in documents) {
                        val pedido = document.toObject(Pedido::class.java)

                        val pedidoLayout = LinearLayout(this)
                        pedidoLayout.orientation = LinearLayout.VERTICAL

                        val pedidoView = TextView(this)
                        pedidoView.text = "Pedido: ${pedido.nombre} - Total: $${pedido.totalCompra}"
                        pedidoView.textSize = 18f
                        pedidoView.setPadding(8, 8, 8, 8)

                        val seleccionarButton = Button(this)
                        seleccionarButton.text = "Seleccionar"
                        seleccionarButton.setOnClickListener {
                            mostrarDireccionEnMapa(pedido)
                        }

                        pedidoLayout.addView(pedidoView)
                        pedidoLayout.addView(seleccionarButton)
                        pedidosContainer.addView(pedidoLayout)
                    }
                }
                progressBar.visibility = View.GONE
            }
            .addOnFailureListener { exception ->
                Log.w("VerPedidosActivity", "Error getting documents: ", exception)
                Toast.makeText(this, "Error al cargar pedidos.", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
            }
    }

    private fun calcularValorDespacho(distanciaKm: Double, totalCompra: Double): Double {
        return when {
            totalCompra >= 50000 && distanciaKm <= 20 -> 0.0
            totalCompra in 25000.0..49999.99 -> distanciaKm * 150
            else -> distanciaKm * 300
        }
    }

    private fun mostrarDireccionEnMapa(pedido: Pedido) {
        val intent = Intent(this, MenuActivity::class.java)
        val geoPoint = pedido.direccion // Asumiendo que direccion es de tipo GeoPoint

        if (geoPoint != null) {
            intent.putExtra("latitud", geoPoint.latitude)
            intent.putExtra("longitud", geoPoint.longitude)

            // Lógica para calcular la distancia
            val distanciaKm = calcularDistancia(geoPoint) // Implementa esta función
            val valorDespacho = calcularValorDespacho(distanciaKm, pedido.totalCompra)
            intent.putExtra("valorDespacho", valorDespacho) // Pasa el valor de despacho
        }

        intent.putExtra("totalCompra", pedido.totalCompra)
        startActivity(intent)
    }

    // Implementa esta función para calcular la distancia según tu lógica
    private fun calcularDistancia(geoPoint: com.google.firebase.firestore.GeoPoint?): Double {
        // Aquí puedes implementar la lógica para calcular la distancia
        // Retorna la distancia en kilómetros
        return 10.0 // Ejemplo, reemplaza con tu lógica
    }
}
