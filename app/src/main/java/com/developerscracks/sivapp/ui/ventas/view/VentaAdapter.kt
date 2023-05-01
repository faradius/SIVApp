package com.developerscracks.sivapp.ui.ventas.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.developerscracks.sivapp.data.model.venta.ProductoVenta
import com.developerscracks.sivapp.databinding.ItemVentaBinding

class VentaAdapter(
    var listaProductos: ArrayList<ProductoVenta>,
    var onClick: OnItemClicked
): RecyclerView.Adapter<VentaAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemVentaBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val producto = listaProductos[position]

        holder.tvNomProducto.text = producto.nomProducto
        holder.tvCantidad.text = producto.cantidad.toString()
        holder.tvTotal.text = (producto.precio * producto.cantidad).toString()

        holder.ibtnMas.setOnClickListener {
            onClick.agregarProducto(producto)
        }

        holder.ibtnMenos.setOnClickListener {
            onClick.quitarProducto(producto)
        }
    }

    override fun getItemCount(): Int {
      return  listaProductos.size
    }

    class ViewHolder(itemBinding: ItemVentaBinding): RecyclerView.ViewHolder(itemBinding.root) {
        val tvNomProducto = itemBinding.tvNomProducto
        val ibtnMenos = itemBinding.ibtnMenos
        val ibtnMas = itemBinding.ibtnMas
        val tvCantidad = itemBinding.tvCantidad
        val tvTotal = itemBinding.tvTotal
    }

    interface OnItemClicked{
        fun agregarProducto(producto: ProductoVenta)
        fun quitarProducto(producto: ProductoVenta)
    }
}