package com.developerscracks.sivapp.data.model.venta

import com.developerscracks.sivapp.data.model.proveedor.Proveedor

data class VentaResponse (
    val codigo: String,
    val mensaje: String,
    val resultado: List<DatosVentas>
)