package com.developerscracks.sivapp.ui.proveedores.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.developerscracks.sivapp.R
import com.developerscracks.sivapp.data.model.proveedor.Proveedor
import com.developerscracks.sivapp.databinding.FragmentProveedoresBinding


class ProveedoresFragment : Fragment(R.layout.fragment_proveedores), ProveedorAdapter.OnItemClickedProv {

    private lateinit var binding: FragmentProveedoresBinding
    private lateinit var adapter: ProveedorAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProveedoresBinding.bind(view)
        (activity as AppCompatActivity).supportActionBar!!.title = "Proveedores"
    }

    private fun setupRecyclerView(){
        binding.rvProveedores.layoutManager = LinearLayoutManager(requireContext())
        adapter = ProveedorAdapter(requireContext(), arrayListOf(), this)
        binding.rvProveedores.adapter = adapter
    }

    override fun llamarProveedor(tel: String) {
        TODO("Not yet implemented")
    }

    override fun enviarEmail(email: String) {
        TODO("Not yet implemented")
    }

    override fun editarProveedor(prov: Proveedor) {
        TODO("Not yet implemented")
    }
}