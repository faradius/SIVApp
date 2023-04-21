package com.developerscracks.sivapp.ui.proveedores.view

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.developerscracks.sivapp.R
import com.developerscracks.sivapp.data.model.proveedor.Proveedor
import com.developerscracks.sivapp.databinding.AlertDialogProveedorBinding
import com.developerscracks.sivapp.databinding.FragmentProveedoresBinding
import com.developerscracks.sivapp.ui.proveedores.viewmodel.ProveedoresViewModel
import es.dmoral.toasty.Toasty


class ProveedoresFragment : Fragment(R.layout.fragment_proveedores), ProveedorAdapter.OnItemClickedProv {

    private lateinit var binding: FragmentProveedoresBinding
    private lateinit var bindingAlertDialog: AlertDialogProveedorBinding
    private lateinit var adapter: ProveedorAdapter
    private lateinit var viewmodel: ProveedoresViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProveedoresBinding.bind(view)
        (activity as AppCompatActivity).supportActionBar!!.title = "Proveedores"

        viewmodel = ViewModelProvider(this)[ProveedoresViewModel::class.java]

        setupRecyclerView()
        viewmodel.getProveedores()

        viewmodel.listaProveedores.observe(viewLifecycleOwner){
            adapter.listaProveedores = it as ArrayList<Proveedor>
            adapter.notifyDataSetChanged()
        }

        viewmodel.mensaje.observe(viewLifecycleOwner){ mensaje ->
            Toasty.info(requireContext(), mensaje, Toasty.LENGTH_SHORT, true).show()
        }

        binding.ibtnAdd.setOnClickListener {
            alertDialogAddUpdate("add")
        }
    }

    private fun alertDialogAddUpdate(accion:String){
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        bindingAlertDialog = AlertDialogProveedorBinding.inflate(inflater)

        builder.setView(bindingAlertDialog.root)

        //Esto es para que no se cierre el alerdialog cuando se presiona fuera de el
        builder.setCancelable(false)

        if (accion == "add"){
            builder.setTitle("Agregar Proveedor")
        }

        val etNomProveedor = bindingAlertDialog.etNomProveedor
        val etTelefono = bindingAlertDialog.etTelefono
        val etEmail = bindingAlertDialog.etEmail

        builder.setPositiveButton("ACEPTAR"){_,_ ->

                viewmodel.validarCampos(
                    accion,
                    etNomProveedor.text.toString().trim(),
                    etTelefono.text.toString().trim(),
                    etEmail.text.toString().trim()
                )

                adapter.notifyDataSetChanged()

        }

        builder.setNegativeButton("CANCELAR"){_,_ ->

        }

        builder.show()
    }

    private fun setupRecyclerView(){
        binding.rvProveedores.layoutManager = LinearLayoutManager(requireContext())
        adapter = ProveedorAdapter(requireContext(), arrayListOf(), this)
        binding.rvProveedores.adapter = adapter
    }

    override fun llamarProveedor(tel: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel: $tel")
        requireContext().startActivity(intent)
    }

    override fun enviarEmail(email: String) {
        val to = arrayOf(email)
        val cc = arrayOf("")

        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.data = Uri.parse("mailto:")
        emailIntent.type = "text/plain"
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to)
        emailIntent.putExtra(Intent.EXTRA_CC, cc)
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Asunto")
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Escribe aqui tu mensaje")

        try {
            requireContext().startActivity(Intent.createChooser(emailIntent, "Enviar email . . ."))
        }catch (ex: ActivityNotFoundException){
            Toasty.error(requireContext(), "No tienes cliente de email instalada", Toasty.LENGTH_SHORT, true).show()
        }
    }

    override fun editarProveedor(prov: Proveedor) {
        //TODO("Not yet implemented")
    }
}