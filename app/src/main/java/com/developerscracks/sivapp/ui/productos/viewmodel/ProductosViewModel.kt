package com.developerscracks.sivapp.ui.productos.viewmodel

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developerscracks.sivapp.data.model.producto.Producto
import com.developerscracks.sivapp.data.model.producto.ProductoResponse
import com.developerscracks.sivapp.data.network.RetrofitClient
import com.developerscracks.sivapp.utils.Permisos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class ProductosViewModel:ViewModel() {

    private var _listaProductos = MutableLiveData<MutableList<Producto>>()
    val listaProductos: LiveData<MutableList<Producto>> get() = _listaProductos

    private var _listaNomProveedores = MutableLiveData<MutableList<String>>()
    val listaNomProveedores:LiveData<MutableList<String>> get() = _listaNomProveedores

    private var _mensaje = MutableLiveData<String>()
    val mensaje:LiveData<String> get() = _mensaje

    private lateinit var response: Response<ProductoResponse>


    fun getProductos(){
        viewModelScope.launch(Dispatchers.IO) {
            val response = RetrofitClient.webService.getProductos()
            withContext(Dispatchers.Main){
                if (response.body()!!.codigo == "200"){
                    _listaProductos.value = response.body()!!.resultado
                }else{
                    _mensaje.value = response.body()!!.mensaje
                }
            }
        }
    }

    fun getNomProveedores(){
        viewModelScope.launch(Dispatchers.IO) {
            val response = RetrofitClient.webService.getProveedores()
            withContext(Dispatchers.Main){
                if (response.body()!!.codigo == "200"){
                    val proveedores = mutableListOf<String>()
                    response.body()!!.resultado.forEach{
                        proveedores.add(it.nomProveedor)
                    }

                    _listaNomProveedores.value = proveedores
                }else{
                    _mensaje.value = response.body()!!.mensaje
                }
            }
        }
    }

    fun filtrarListaProductos(producto: String){
        val listaFiltrada: MutableList<Producto> = mutableListOf()
        for (prod in _listaProductos.value!!){
            if (prod.codProducto.contains(producto) || prod.nomProducto.contains(producto)){
                listaFiltrada.add(prod)
            }
        }

        _listaProductos.value = listaFiltrada
    }

    fun validarCampos(
      accion: String,
      codigo: String,
      nomProducto: String,
      descripcion: String,
      nomProveedor: String,
      precio: String,
      almacen: String
    ){
        if (
            codigo.isNullOrEmpty()
            || nomProducto.isNullOrEmpty()
            || descripcion.isNullOrEmpty()
            || nomProveedor.isNullOrEmpty()
            || precio.isNullOrEmpty()
            || almacen.isNullOrEmpty()
        ){
            _mensaje.value = "Todos los campos deben ser llenados"
        }else{
            val prod = Producto(
                almacen = almacen.toInt(),
                codProducto = codigo,
                descripcion = descripcion,
                nomProducto = nomProducto,
                nomProveedor = nomProveedor,
                precio = precio.toDouble()
            )

            productoAddUpdate(accion, prod)
        }
    }

    private fun productoAddUpdate(accion: String, prod: Producto) {
        viewModelScope.launch(Dispatchers.IO) {
            if (accion == "add"){
                response = RetrofitClient.webService.addProducto(prod)
            }else{
                response = RetrofitClient.webService.updateProducto(prod.codProducto, prod)
            }

            withContext(Dispatchers.Main){
                if (response.body()!!.codigo == "200"){
                    _mensaje.value = response.body()!!.mensaje
                    getProductos()
                }else{
                    _mensaje.value = response.body()!!.mensaje
                }
            }
        }
    }


    fun checkCamaraPermiso(activity: Activity): Boolean{
        val isPermiso = Permisos().checkCamaraPermiso(activity)
        return isPermiso
    }
}