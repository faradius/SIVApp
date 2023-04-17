package com.developerscracks.sivapp.data.model.proveedor

import com.developerscracks.sivapp.data.model.producto.Producto

data class ProveedorResponse(
    val codigo: String,
    val mensaje: String,
    val resultado: MutableList<Proveedor>
)
