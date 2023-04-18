package com.developerscracks.sivapp.ui.productos.view

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.developerscracks.sivapp.R
import com.developerscracks.sivapp.data.model.producto.Producto
import com.developerscracks.sivapp.databinding.AlertDialogProductoBinding
import com.developerscracks.sivapp.databinding.FragmentProductosBinding
import com.developerscracks.sivapp.ui.productos.viewmodel.ProductosViewModel


class ProductosFragment : Fragment(R.layout.fragment_productos), ProductoAdapter.OnItemClicked {

    private lateinit var binding: FragmentProductosBinding
    private lateinit var bindingAlert: AlertDialogProductoBinding
    private lateinit var adapter: ProductoAdapter

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

        binding.ibtnAdd.setOnClickListener {
            alertDialogAddUpdate()
        }
    }

    private fun setupRecyclerView(){
        binding.rvProductos.layoutManager = LinearLayoutManager(requireActivity())
        adapter = ProductoAdapter(arrayListOf(), this)
        binding.rvProductos.adapter = adapter
    }

    private fun alertDialogAddUpdate(){
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        bindingAlert = AlertDialogProductoBinding.inflate(inflater)

        builder.setView(bindingAlert.root)
        builder.setCancelable(false)

        val ibtnEscaner = bindingAlert.ibtnEscaner
        val etNomProducto = bindingAlert.etNomProducto
        val etDescripcion = bindingAlert.etDescripcion
        val spiProveedores = bindingAlert.spiProveedor
        val tvNomProveedor = bindingAlert.tvNomProveedor
        val etPrecio = bindingAlert.etPrecio
        val etAlmacen = bindingAlert.etAlmacen

        viewModel.getNomProveedores()

        viewModel.listaNomProveedores.observe(viewLifecycleOwner){
            spiProveedores.adapter = ArrayAdapter(
                requireContext(), android.R.layout.simple_spinner_item, it
            )
        }

        builder.setPositiveButton("ACEPTAR"){ _,_ ->

        }

        builder.setNegativeButton("CANCELAR"){ _,_ ->

        }

        builder.show()
    }

    override fun editarProducto(prod: Producto) {
        //TODO("Not yet implemented")
    }

}