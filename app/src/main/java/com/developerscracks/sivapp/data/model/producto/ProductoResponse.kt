package com.developerscracks.sivapp.data.model.producto

data class ProductoResponse(
    val codigo: String,
    val mensaje: String,
    val resultado: MutableList<Producto>
)