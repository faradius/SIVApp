package com.developerscracks.sivapp.ui.productos.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.developerscracks.sivapp.data.model.producto.Producto
import com.developerscracks.sivapp.databinding.ItemProductosBinding

class ProductoAdapter(var listaProductos: ArrayList<Producto>, val onClick: OnItemClicked): RecyclerView.Adapter<ProductoAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoAdapter.ViewHolder {
        return ViewHolder(ItemProductosBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ProductoAdapter.ViewHolder, position: Int) {
        val producto = listaProductos[position]

        holder.tvIdProducto.text = producto.codProducto
        holder.tvNomProducto.text = "Producto: ${producto.nomProducto}"
        holder.tvDescripcion.text = "Descripción: ${producto.descripcion}"
        holder.tvNomProveedor.text = "Proveedor: ${producto.nomProveedor}"
        holder.tvAlmacen.text = "${producto.almacen}"
        holder.tvPrecio.text = "$${producto.precio}"

        holder.cvProducto.setOnClickListener {
            onClick.editarProducto(producto)
        }
    }

    override fun getItemCount(): Int {
        return listaProductos.size
    }

    class ViewHolder(itemBinding: ItemProductosBinding): RecyclerView.ViewHolder(itemBinding.root) {
        val cvProducto = itemBinding.cvProducto
        val tvIdProducto = itemBinding.tvIdProducto
        val tvNomProducto = itemBinding.tvNomProducto
        val tvDescripcion = itemBinding.tvDescripcion
        val tvNomProveedor = itemBinding.tvNomProveedor
        val tvAlmacen = itemBinding.tvAlmacen
        val tvPrecio = itemBinding.tvPrecio
    }

    interface OnItemClicked{
        fun editarProducto(prod: Producto)
    }
}