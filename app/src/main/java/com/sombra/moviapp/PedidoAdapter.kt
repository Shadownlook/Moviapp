// PedidoAdapter.kt
package com.sombra.moviapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PedidoAdapter(
    private val listaPedidos: List<Pedido>,
    private val onClick: (Pedido) -> Unit
) : RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder>() {

    // ViewHolder para el RecyclerView
    class PedidoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombrePedido: TextView = itemView.findViewById(R.id.nombrePedidoTextView) // Asegúrate de que este ID coincida con el XML
        val precioPedido: TextView = itemView.findViewById(R.id.precioPedidoTextView) // Asegúrate de que este ID coincida con el XML
        // Agrega más campos según tu layout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
        // Inflar el layout para cada elemento del RecyclerView
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pedido, parent, false)
        return PedidoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
        val pedido = listaPedidos[position]

        // Asignar los valores a los TextViews desde el objeto Pedido
        holder.nombrePedido.text = pedido.nombre // Asegúrate de que el campo `nombre` exista en tu data class `Pedido`
        holder.precioPedido.text = "$${pedido.totalCompra}" // Asegúrate de que el campo `totalCompra` exista

        // Establece un click listener para el elemento del pedido
        holder.itemView.setOnClickListener {
            onClick(pedido) // Llama a la función de callback al hacer clic en el pedido
        }
    }

    override fun getItemCount(): Int {
        return listaPedidos.size // Retorna el tamaño de la lista de pedidos
    }
}
