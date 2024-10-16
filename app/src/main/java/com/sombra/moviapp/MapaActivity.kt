package com.sombra.moviapp

import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MapaActivity : AppCompatActivity() {

    private lateinit var map: MapView
    private lateinit var pedido: Pedido

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa)

        // Inicializa la configuración del mapa
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))

        // Inicializa el mapa
        map = findViewById(R.id.map)
        map.setBuiltInZoomControls(true)
        map.setMultiTouchControls(true)

        // Recibir el pedido
        pedido = intent.getParcelableExtra("pedido") ?: throw IllegalArgumentException("Pedido no encontrado")

        // Configurar el mapa
        configurarMapa()
    }

    private fun configurarMapa() {
        // Aquí deberías implementar la lógica para obtener la distancia real
        val distancia = 10.0 // Ejemplo: distancia en km (deberías calcularla según la ubicación del pedido)

        // Calcula la tarifa
        val tarifa = calcularTarifa(pedido.totalCompra, distancia)

        // Muestra la tarifa en un Toast
        Toast.makeText(this, "Tarifa de despacho: $tarifa", Toast.LENGTH_LONG).show()

        // Marcar la ubicación en el mapa
        val point = GeoPoint(-33.4489, -70.6693) // Aquí debes colocar las coordenadas del pedido
        val marker = Marker(map).apply {
            position = point
            title = "Tarifa: $tarifa"
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        }
        map.overlays.add(marker)

        // Configurar el centro y el zoom del mapa
        map.controller.setZoom(15.0)
        map.controller.setCenter(point)
    }

    private fun calcularTarifa(totalCompra: Double, distancia: Double): Double {
        return when {
            totalCompra > 50000 -> 0.0 // Despacho gratis
            totalCompra in 25000.0..49999.0 -> distancia * 150 // $150 por km
            else -> distancia * 300 // $300 por km
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

