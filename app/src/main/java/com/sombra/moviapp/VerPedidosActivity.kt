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
import org.osmdroid.util.GeoPoint

class VerPedidosActivity : AppCompatActivity() {

    private lateinit var pedidosContainer: LinearLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_pedidos)

        // Inicializa Firestore
        firestore = FirebaseFirestore.getInstance()

        // Configura el contenedor de pedidos y el ProgressBar
        pedidosContainer = findViewById(R.id.pedidosContainer)
        progressBar = findViewById(R.id.progressBar)

        // Cargar pedidos de Firestore
        cargarPedidos()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun cargarPedidos() {
        progressBar.visibility = View.VISIBLE // Muestra el ProgressBar
        pedidosContainer.removeAllViews() // Limpiar contenedor de pedidos

        firestore.collection("pedidos")
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Toast.makeText(this, "No hay pedidos disponibles.", Toast.LENGTH_SHORT).show()
                } else {
                    for (document in documents) {
                        val pedido = document.toObject(Pedido::class.java)

                        // Crear un layout para cada pedido
                        val pedidoLayout = LinearLayout(this)
                        pedidoLayout.orientation = LinearLayout.VERTICAL

                        // Agregar TextView para el pedido
                        val pedidoView = TextView(this)
                        pedidoView.text = "Pedido: ${pedido.nombre} - Total: $${pedido.totalCompra}"
                        pedidoView.textSize = 18f
                        pedidoView.setPadding(8, 8, 8, 8)

                        // Agregar botón de selección
                        val seleccionarButton = Button(this)
                        seleccionarButton.text = "Seleccionar"
                        seleccionarButton.setOnClickListener {
                            // Lógica para manejar la selección
                            mostrarDireccionEnMapa(pedido)
                        }

                        // Agregar vistas al layout
                        pedidoLayout.addView(pedidoView)
                        pedidoLayout.addView(seleccionarButton)
                        pedidosContainer.addView(pedidoLayout)
                    }
                }
                progressBar.visibility = View.GONE // Oculta el ProgressBar
            }
            .addOnFailureListener { exception ->
                Log.w("VerPedidosActivity", "Error getting documents: ", exception)
                Toast.makeText(this, "Error al cargar pedidos.", Toast.LENGTH_SHORT).show() // Notificar error
                progressBar.visibility = View.GONE // Oculta el ProgressBar en caso de error
            }
    }

    // Función para mostrar la dirección del pedido en el mapa
    private fun mostrarDireccionEnMapa(pedido: Pedido) {
        val intent = Intent(this, MenuActivity::class.java)
        val geoPoint = pedido.direccion // Obtener el GeoPoint de dirección
        if (geoPoint != null) {
            intent.putExtra("latitud", geoPoint.latitude)
        } // Pasa la latitud
        if (geoPoint != null) {
            intent.putExtra("longitud", geoPoint.longitude)
        } // Pasa la longitud
        intent.putExtra("totalCompra", pedido.totalCompra) // Pasa el total de la compra
        startActivity(intent)
    }
}
