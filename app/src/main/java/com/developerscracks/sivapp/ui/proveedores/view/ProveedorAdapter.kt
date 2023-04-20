package com.developerscracks.sivapp.ui.proveedores.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.developerscracks.sivapp.data.model.proveedor.Proveedor
import com.developerscracks.sivapp.databinding.ItemProveedorBinding

class ProveedorAdapter(
    var context: Context,
    var listaProveedores: ArrayList<Proveedor>,
    var onClick: OnItemClickedProv
): RecyclerView.Adapter<ProveedorAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProveedorAdapter.ViewHolder {
        return ViewHolder(ItemProveedorBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ProveedorAdapter.ViewHolder, position: Int) {
        val proveedor = listaProveedores[position]

        holder.tvNomProveedor.text = proveedor.nomProveedor

        holder.ibtnTelefono.setOnClickListener {
            onClick?.llamarProveedor(proveedor.telefono)
        }

        holder.ibtnEmail.setOnClickListener {
            onClick?.enviarEmail(proveedor.email)
        }

        holder.ibtnEditar.setOnClickListener {
            onClick?.editarProveedor(proveedor)
        }
    }

    override fun getItemCount(): Int {
        return listaProveedores.size
    }

    class ViewHolder(itemBinding: ItemProveedorBinding): RecyclerView.ViewHolder(itemBinding.root) {
        val tvNomProveedor = itemBinding.tvNomProveedor
        val ibtnTelefono = itemBinding.ibtnTelefono
        val ibtnEmail = itemBinding.ibtnEmail
        val ibtnEditar = itemBinding.ibtnEditar
    }

    interface OnItemClickedProv{
        fun llamarProveedor(tel: String)
        fun enviarEmail(email:String)
        fun editarProveedor(prov:Proveedor)
    }
}