package com.developerscracks.sivapp.ui.proveedores.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developerscracks.sivapp.data.model.proveedor.Proveedor
import com.developerscracks.sivapp.data.model.proveedor.ProveedorResponse
import com.developerscracks.sivapp.data.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class ProveedoresViewModel: ViewModel() {

    private var _listaProveedores = MutableLiveData<MutableList<Proveedor>>()
    val listaProveedores:LiveData<MutableList<Proveedor>> get() = _listaProveedores

    private var _mensaje = MutableLiveData<String>()
    val mensaje:LiveData<String> get() = _mensaje

    private lateinit var response: Response<ProveedorResponse>

    fun getProveedores(){
        viewModelScope.launch(Dispatchers.IO){
            response = RetrofitClient.webService.getProveedores()
            withContext(Dispatchers.Main){
                if (response.body()!!.codigo == "200"){
                    _listaProveedores.value = response.body()!!.resultado
                }else{
                    _mensaje.value = response.body()!!.mensaje
                }
            }
        }
    }

    fun filtrarListaProveedores(nomProv: String){
        val listaFiltrada: MutableList<Proveedor> = mutableListOf()
        for(proveedor in listaProveedores.value!!){
            if (proveedor.nomProveedor.contains(nomProv)){
                listaFiltrada.add(proveedor)
            }
        }
        _listaProveedores.value = listaFiltrada
    }

    fun validarCampos(accion:String, nomProv:String, tel:String, email: String){
        if (nomProv.isNullOrEmpty() || tel.isNullOrEmpty() || email.isNullOrEmpty()){
            _mensaje.value = "Todos los campos deben llenarse"
        }else{
            val prov = Proveedor(email, nomProv, tel)
            proveedorAddUpdate(accion, prov)
        }
    }

    fun proveedorAddUpdate(accion: String, proveedor: Proveedor){
        viewModelScope.launch(Dispatchers.IO) {
            if (accion == "add"){
                response = RetrofitClient.webService.addProveedor(proveedor)
            }else if (accion == "update"){
                response = RetrofitClient.webService.updateProveedor(proveedor.nomProveedor, proveedor)
            }

            withContext(Dispatchers.Main){
                if (response.body()!!.codigo == "200"){
                    _mensaje.value = response.body()!!.mensaje
                    getProveedores()
                }else{
                    _mensaje.value = response.body()!!.mensaje
                }
            }
        }
    }
}