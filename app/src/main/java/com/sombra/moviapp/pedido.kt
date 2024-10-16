package com.sombra.moviapp

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.GeoPoint

data class Pedido(
    val id: String = "", // ID del pedido
    val nombre: String = "", // Nombre del pedido
    val totalCompra: Double = 0.0, // Total de la compra
    val direccion: GeoPoint? = null // Dirección del pedido como GeoPoint
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readParcelable(GeoPoint::class.java.classLoader) // Leer GeoPoint
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(nombre)
        parcel.writeDouble(totalCompra)
        parcel.writeParcelable(direccion, flags) // Escribir GeoPoint
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Pedido> {
        override fun createFromParcel(parcel: Parcel): Pedido {
            return Pedido(parcel)
        }

        override fun newArray(size: Int): Array<Pedido?> {
            return arrayOfNulls(size)
        }
    }
}

private fun Parcel.writeParcelable(direccion: GeoPoint?, flags: Int) {

}
