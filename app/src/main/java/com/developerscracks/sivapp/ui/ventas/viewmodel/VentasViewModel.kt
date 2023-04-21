package com.developerscracks.sivapp.ui.ventas.viewmodel

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developerscracks.sivapp.data.model.venta.ProductoVenta
import com.developerscracks.sivapp.data.model.venta.VentasSend
import com.developerscracks.sivapp.data.network.RetrofitClient
import com.developerscracks.sivapp.utils.Permisos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class VentasViewModel : ViewModel() {

    private var _listaProductos = MutableLiveData<MutableList<ProductoVenta>>()
    val listaProductos: LiveData<MutableList<ProductoVenta>> get() = _listaProductos

    private var _mensaje = MutableLiveData<String>()
    val mensaje: LiveData<String> get() = _mensaje

    private var _cambio = MutableLiveData<Double>()
    val cambio: LiveData<Double> get() = _cambio

    private var _totalVenta = MutableLiveData<Double>()
    val totalVenta: LiveData<Double> get() = _totalVenta

    fun checkCamaraPermiso(activity: Activity): Boolean {
        val isPermiso = Permisos().checkCamaraPermiso(activity)
        return isPermiso
    }

    fun validarCampo(codBar: String) {
        if (codBar.isEmpty()) {
            _mensaje.value = "Debes poner un codigo para la busqueda"
        } else {
            getProducto(codBar)
        }
    }

    fun getProducto(codBar: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = RetrofitClient.webService.getProducto(codBar)

            withContext(Dispatchers.Main) {
                if (response.body()!!.codigo == "200") {
                    if (response.body()!!.resultado.size > 0) {
                        if (_listaProductos.value.isNullOrEmpty()) {
                            _listaProductos.value = arrayListOf()

                            _listaProductos.value!!.add(
                                ProductoVenta(
                                    response.body()!!.resultado[0].almacen,
                                    response.body()!!.resultado[0].codProducto,
                                    response.body()!!.resultado[0].descripcion,
                                    response.body()!!.resultado[0].nomProducto,
                                    response.body()!!.resultado[0].nomProveedor,
                                    response.body()!!.resultado[0].precio,
                                    1
                                )
                            )

                            _totalVenta.value = response.body()!!.resultado[0].precio
                        } else {
                            var existe = false
                            _listaProductos.value!!.forEach {
                                if (it.codProducto == response.body()!!.resultado[0].codProducto) {
                                    it.cantidad = it.cantidad + 1
                                    calcularTotalVenta()
                                    existe = true
                                }
                            }

                            if (!existe) {
                                _listaProductos.value!!.add(
                                    ProductoVenta(
                                        response.body()!!.resultado[0].almacen,
                                        response.body()!!.resultado[0].codProducto,
                                        response.body()!!.resultado[0].descripcion,
                                        response.body()!!.resultado[0].nomProducto,
                                        response.body()!!.resultado[0].nomProveedor,
                                        response.body()!!.resultado[0].precio,
                                        1
                                    )
                                )

                                calcularTotalVenta()
                                existe = false
                            }
                        }
                    }
                }else{
                    _mensaje.value = response.body()!!.mensaje
                }
            }
        }
    }

    fun calcularTotalVenta(){
        _totalVenta.value = 0.0
        _listaProductos.value!!.forEach{
            _totalVenta.value = _totalVenta.value!! + (it.cantidad * it.precio)
        }
    }

    fun agregarProducto(prodVenta: ProductoVenta){
        _listaProductos.value!!.forEach{
            if (it.codProducto == prodVenta.codProducto){
                it.cantidad += 1
                calcularTotalVenta()
            }
        }
    }

    fun quitarProducto(prodVenta: ProductoVenta){
        _listaProductos.value!!.forEach{
            if (it.codProducto == prodVenta.codProducto){
                if (it.cantidad == 1){
                    _listaProductos.value!!.remove(it)
                    calcularTotalVenta()
                    return
                }else{
                    it.cantidad -= 1
                    calcularTotalVenta()
                    return
                }
            }
        }
    }

    fun validarCamposVenta(pago: Double, totalVenta: Double){
        if (pago <= 0 || pago < totalVenta || totalVenta <= 0){
            _mensaje.value = "Revisa la cantidad del pago ya que no puede ser menor al total o menor o igual a 0"
        }else{
            if (_listaProductos.value?.size == 0){
                _mensaje.value = "Debes poner productos en la lista"
            }else{
                registrarVenta(pago, totalVenta)
            }
        }
    }

    private fun registrarVenta(pago: Double, totalVenta: Double) {
        var stringVenta = ""
        val tiempo = System.currentTimeMillis().toString()
        val fechaVenta = SimpleDateFormat("yyyy-MM-dd").format(Date())

        _listaProductos.value!!.forEach {
            if (stringVenta == ""){
                stringVenta = "${it.codProducto}_${it.cantidad}_${it.precio}"
            }else{
                stringVenta += ",${it.codProducto}_${it.cantidad}_${it.precio}"
            }
        }

        val datosSend = VentasSend(
            tiempo,
            stringVenta,
            fechaVenta,
            totalVenta
        )

        viewModelScope.launch(Dispatchers.IO){
            val response = RetrofitClient.webService.addVenta(datosSend)
            withContext(Dispatchers.Main){
                if (response.body()!!.codigo == "200"){
                    isCambio(pago, totalVenta)
                }
            }
        }
    }

    private fun isCambio(pago: Double, totalVenta: Double): Boolean {
        return if ((pago - totalVenta) < 0.0){
            _mensaje.value = "No puedes realizar la venta ya que el pago no cubre la totalidad"
            false
        }else if ((pago - totalVenta) == 0.0){
            _cambio.value = 0.0
            _mensaje.value = "Gracias por la compra"
            true
        }else{
            _cambio.value = pago - totalVenta
            true
        }
    }

    fun resetVenta(){
        _listaProductos.value!!.clear()
    }
}