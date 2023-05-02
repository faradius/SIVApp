package com.developerscracks.sivapp.ui.ventas.view

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

class VentasFragment : Fragment(R.layout.fragment_ventas) {

    private lateinit var binding: FragmentVentasBinding
    private lateinit var adapter: VentaAdapter

    private lateinit var viewModel: VentasViewModel

    private var cambio = 0.0

    private var codigoLeido = ""
    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result ->
        codigoLeido = ""
        if (result.contents == null) {
            Toasty.error(requireContext(), "CANCELADO", Toasty.LENGTH_SHORT, true).show()
        } else {
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

        viewModel.listaProductos.observe(viewLifecycleOwner) {
            adapter.listaProductos = it as ArrayList<ProductoVenta>
            adapter.notifyDataSetChanged()
        }

        viewModel.mensaje.observe(viewLifecycleOwner) {
            Toasty.info(requireContext(), it, Toasty.LENGTH_SHORT, true).show()
        }

        viewModel.cambio.observe(viewLifecycleOwner) {
            cambio = it
        }

        viewModel.totalVenta.observe(viewLifecycleOwner) {
            binding.tvTotal.text = "$it"
        }

        binding.ibtnEscanear.setOnClickListener {
            val isPermiso = viewModel.checkCamaraPermiso(requireActivity())
            if (isPermiso) {
                barcodeLauncher.launch(ScanOptions())
            }
        }

        binding.ibtnBuscar.setOnClickListener {
            viewModel.validarCampo(binding.etCodBarr.text.toString().trim())
            adapter.notifyDataSetChanged()
        }

        binding.btnRegistrarVenta.setOnClickListener {
            viewModel.validarCamposVenta(
                binding.etPago.text.toString().trim().toDouble(),
                binding.tvTotal.text.toString().trim().toDouble()
            )

            val cambio = viewModel.isCambio(
                binding.etPago.text.toString().trim().toDouble(),
                binding.tvTotal.text.toString().trim().toDouble()
            )

            if (cambio){
                alertDialogCambio()
            }
        }
    }

    private fun alertDialogCambio(){
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater

        val vista = inflater.inflate(R.layout.alert_dialog_cambio, null)
        builder.setView(vista)
        builder.setCancelable(false)

        val tvCambio = vista.findViewById(R.id.tvCambio) as TextView

        tvCambio.text = viewModel.cambio.value.toString()

        builder.setPositiveButton("ACEPTAR"){_,_->
            binding.etCodBarr.setText("")
            binding.etPago.setText("")
            binding.tvTotal.text = "0.0"
            viewModel.resetVenta()
            adapter.notifyDataSetChanged()
        }

        builder.show()
    }

    private fun setupRecyclerView() {
        binding.rvVentaProductos.layoutManager = LinearLayoutManager(requireActivity())
        adapter = VentaAdapter(
            listaProductos = arrayListOf(),
            agregarProducto = { producto ->
                viewModel.agregarProducto(producto)
                adapter.notifyDataSetChanged()

            },
            quitarProducto = { producto ->
                viewModel.quitarProducto(producto)
                adapter.notifyDataSetChanged()
            }
        )
        binding.rvVentaProductos.adapter = adapter
    }
}