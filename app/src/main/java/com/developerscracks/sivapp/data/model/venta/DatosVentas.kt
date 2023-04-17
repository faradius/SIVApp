package com.developerscracks.sivapp.data.model.venta

data class DatosVentas(
    val fechaVenta: String,
    val idVenta: String,
    val codProductos: List<ProdsVenta>,
    val total: Double
)