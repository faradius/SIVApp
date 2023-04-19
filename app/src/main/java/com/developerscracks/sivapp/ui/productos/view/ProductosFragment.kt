package com.developerscracks.sivapp.ui.productos.view

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.developerscracks.sivapp.R
import com.developerscracks.sivapp.data.model.producto.Producto
import com.developerscracks.sivapp.databinding.AlertDialogProductoBinding
import com.developerscracks.sivapp.databinding.FragmentProductosBinding
import com.developerscracks.sivapp.ui.productos.viewmodel.ProductosViewModel
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import es.dmoral.toasty.Toasty


class ProductosFragment : Fragment(R.layout.fragment_productos), ProductoAdapter.OnItemClicked {

    private lateinit var binding: FragmentProductosBinding
    private lateinit var bindingAlertDialog: AlertDialogProductoBinding
    private lateinit var adapter: ProductoAdapter

    private lateinit var etCodigo:EditText

    //Este codigo realiza el escaneo del codigo de barras
    private var codigoLeido = ""
    private val barcodeLauncher = registerForActivityResult(ScanContract()){result ->
        codigoLeido = ""
        if(result.contents == null){
            Toasty.error(requireContext(), "CANCELADO", Toasty.LENGTH_SHORT, true).show()
        }else{
            codigoLeido = result.contents.toString()
            etCodigo.setText(codigoLeido)
        }

    }

    private lateinit var viewModel: ProductosViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProductosBinding.bind(view)

        viewModel = ViewModelProvider(this)[ProductosViewModel::class.java]

        setupRecyclerView()
        viewModel.getProductos()

        viewModel.listaProductos.observe(viewLifecycleOwner){
            adapter.listaProductos = it as ArrayList<Producto>
            adapter.notifyDataSetChanged()
        }

        viewModel.mensaje.observe(viewLifecycleOwner){mensaje->
            Toasty.info(requireContext(), mensaje, Toasty.LENGTH_SHORT, true).show()
        }

        binding.ibtnAdd.setOnClickListener {
            alertDialogAddUpdate("add")
        }
    }

    private fun setupRecyclerView(){
        binding.rvProductos.layoutManager = LinearLayoutManager(requireActivity())
        adapter = ProductoAdapter(arrayListOf(), this)
        binding.rvProductos.adapter = adapter
    }

    private fun alertDialogAddUpdate(
        accion: String,
        idProducto: String = "",
        nomProducto: String = "",
        descripcion: String = "",
        nomProveedor: String = "",
        almacen: Int = -1,
        precio: Double = -1.0
    ){
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        bindingAlertDialog = AlertDialogProductoBinding.inflate(inflater)

        builder.setView(bindingAlertDialog.root)

        if(accion == "add"){
            builder.setTitle("Agregar producto")
        }else{
            builder.setTitle("Editar Producto")
        }

        builder.setCancelable(false)

        etCodigo = bindingAlertDialog.etCodigo
        val ibtnEscaner = bindingAlertDialog.ibtnEscaner
        val etNomProducto = bindingAlertDialog.etNomProducto
        val etDescripcion = bindingAlertDialog.etDescripcion
        val spiProveedores = bindingAlertDialog.spiProveedor
        val tvNomProveedor = bindingAlertDialog.tvNomProveedor
        val etPrecio = bindingAlertDialog.etPrecio
        val etAlmacen = bindingAlertDialog.etAlmacen

        viewModel.getNomProveedores()

        viewModel.listaNomProveedores.observe(viewLifecycleOwner){
            spiProveedores.adapter = ArrayAdapter(
                requireContext(), android.R.layout.simple_spinner_item, it
            )
        }

        ibtnEscaner.setOnClickListener {
            val isPermiso = viewModel.checkCamaraPermiso(requireActivity())
            if (isPermiso){
                barcodeLauncher.launch(ScanOptions())
            }
        }

        if(accion == "update"){
            etCodigo.setText(idProducto)
            etCodigo.isEnabled = false
            ibtnEscaner.visibility = View.GONE
            etNomProducto.setText(nomProducto)
            etDescripcion.setText(descripcion)
            tvNomProveedor.setText(nomProveedor)
            etPrecio.setText(precio.toString())
            etAlmacen.setText(almacen.toString())
        }


        builder.setPositiveButton("ACEPTAR"){ _,_ ->
            viewModel.validarCampos(
                accion,
                etCodigo.text.toString().trim(),
                etNomProducto.text.toString().trim(),
                etDescripcion.text.toString().trim(),
                spiProveedores.selectedItem.toString(),
                etPrecio.text.toString().trim(),
                etAlmacen.text.toString().trim()
            )

            adapter.notifyDataSetChanged()
        }

        builder.setNegativeButton("CANCELAR"){ _,_ ->

        }

        builder.show()
    }

    override fun editarProducto(prod: Producto) {
        alertDialogAddUpdate(
            "update",
            prod.codProducto,
            prod.nomProducto,
            prod.descripcion,
            prod.nomProveedor,
            prod.almacen,
            prod.precio
        )
    }

}