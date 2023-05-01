package com.developerscracks.sivapp.ui.ventas.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.developerscracks.sivapp.R
import com.developerscracks.sivapp.data.model.venta.ProductoVenta
import com.developerscracks.sivapp.databinding.FragmentVentasBinding
import com.developerscracks.sivapp.ui.ventas.viewmodel.VentasViewModel
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import es.dmoral.toasty.Toasty

class VentasFragment : Fragment(R.layout.fragment_ventas), VentaAdapter.OnItemClicked {

    private lateinit var binding: FragmentVentasBinding
    private lateinit var adapter: VentaAdapter

    private lateinit var viewModel: VentasViewModel

    private var codigoLeido = ""
    private val barcodeLauncher = registerForActivityResult(ScanContract()){result->
        codigoLeido = ""
        if (result.contents == null){
            Toasty.error(requireContext(), "CANCELADO", Toasty.LENGTH_SHORT, true).show()
        }else{
            codigoLeido = result.contents.toString()
            binding.etCodBarr.setText(codigoLeido)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentVentasBinding.bind(view)
        (activity as AppCompatActivity).supportActionBar!!.title = "Ventas"

        viewModel = ViewModelProvider(this)[VentasViewModel::class.java]

        setupRecyclerView()

        binding.ibtnEscanear.setOnClickListener {
            val isPermiso = viewModel.checkCamaraPermiso(requireActivity())
            if (isPermiso){
                barcodeLauncher.launch(ScanOptions())
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvVentaProductos.layoutManager = LinearLayoutManager(requireActivity())
        adapter = VentaAdapter(arrayListOf(), this)
        binding.rvVentaProductos.adapter = adapter
    }

    override fun agregarProducto(producto: ProductoVenta) {

    }

    override fun quitarProducto(producto: ProductoVenta) {

    }
}