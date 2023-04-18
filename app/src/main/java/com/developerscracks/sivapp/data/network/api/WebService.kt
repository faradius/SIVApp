package com.developerscracks.sivapp.data.network.api

import com.developerscracks.sivapp.data.model.producto.Producto
import com.developerscracks.sivapp.data.model.producto.ProductoResponse
import com.developerscracks.sivapp.data.model.proveedor.Proveedor
import com.developerscracks.sivapp.data.model.proveedor.ProveedorResponse
import com.developerscracks.sivapp.data.model.venta.VentaResponse
import com.developerscracks.sivapp.data.model.venta.VentasSend
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface WebService {

    //Proveedores
    @GET("/proveedores")
    suspend fun getProveedores(): Response<ProveedorResponse>

    @POST("/proveedores/add")
    suspend fun addProveedor(@Body prov: Proveedor):Response<ProveedorResponse>

    @PUT("/proveedores/update/{nomProveedor}")
    suspend fun updateProveedor(
        @Path("nomProveedor") nomProveedor: String,
        @Body prov: Proveedor
    ): Response<ProveedorResponse>

    //Productos
    @GET("/productos")
    suspend fun getProductos(): Response<ProductoResponse>

    @GET("/productos/{codProducto}")
    suspend fun getProducto(
        @Path("codProducto") codProducto: String
    ): Response<ProductoResponse>

    @POST("/productos/add")
    suspend fun addProducto(
        @Body prod: Producto
    ): Response<ProductoResponse>

    @PUT("/productos/update/{codProducto}")
    suspend fun updateProducto(
        @Path("codProducto") codProducto: String,
        @Body prod: Producto
    ): Response<ProductoResponse>

    //Ventas
    @GET("/ventas/periodo")
    suspend fun getVentasPeriodo(
        @Query("fechaInicio") fechaInicio: String,
        @Query("fechaFinal") fechaFinal: String
    ): Response<VentaResponse>

    @POST("/ventas/add")
    suspend fun addVenta(
        @Body datosSend: VentasSend
    ): Response<VentaResponse>

}