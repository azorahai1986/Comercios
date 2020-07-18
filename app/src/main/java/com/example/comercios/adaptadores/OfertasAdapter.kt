package com.example.comercios.adaptadores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.comercios.MainActivity
import com.example.comercios.R
import com.example.comercios.modelos.Ofertas
import kotlinx.android.synthetic.main.item_recycler_ofertas.view.*

class OfertasAdapter(var itemsOfertas:ArrayList<Ofertas>, val activityOfertas: MainActivity):RecyclerView.Adapter<OfertasAdapter.OfertasViewHolder>(){

    fun setDataOfertas(data:ArrayList<Ofertas>){
        itemsOfertas = data
    }

    inner class OfertasViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfertasAdapter.OfertasViewHolder =
        OfertasViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_ofertas, parent, false))

    override fun getItemCount(): Int = itemsOfertas.size

    override fun onBindViewHolder(holder: OfertasAdapter.OfertasViewHolder, position: Int) {
        val itemsImagenes = itemsOfertas[position]
        Glide.with(activityOfertas).load(itemsImagenes.imagen).into(holder.itemView.imageViewOfertas)
    }
}