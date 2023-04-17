package com.developerscracks.sivapp.data.model.producto

data class Producto(
    val almacen: Int,
    val codProducto: String,
    val descripcion: String,
    val nomProducto: String,
    val nomProveedor: String,
    val precio: Double
)