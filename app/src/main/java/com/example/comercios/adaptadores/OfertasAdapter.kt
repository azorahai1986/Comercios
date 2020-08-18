package com.example.comercios.adaptadores

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.comercios.MainActivity
import com.example.comercios.R
import com.example.comercios.actividades.CrearOfertas
import com.example.comercios.modelos.Ofertas
import kotlinx.android.synthetic.main.activity_crear_ofertas.*
import kotlinx.android.synthetic.main.activity_crear_ofertas.view.*
import kotlinx.android.synthetic.main.item_recycler_ofertas.view.*

class OfertasAdapter(var itemsOfertas:ArrayList<Ofertas>, val activityOfertas: MainActivity, val crearOfertas: CrearOfertas):RecyclerView.Adapter<OfertasAdapter.OfertasViewHolder>(){

    var arrayColores: ArrayList<Ofertas> = ArrayList()
    fun setDataOfertas(data:ArrayList<Ofertas>){
        itemsOfertas = data
    }

    inner class OfertasViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfertasAdapter.OfertasViewHolder =
        OfertasViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_ofertas, parent, false))

    override fun getItemCount(): Int = itemsOfertas.size

    override fun onBindViewHolder(holder: OfertasAdapter.OfertasViewHolder, position: Int) {
        val itemsImagenes = itemsOfertas[position]


        holder.itemView.textview_precio_item_oferta.text = itemsImagenes.precioOferta
        Glide.with(activityOfertas).load(itemsImagenes.imagen).into(holder.itemView.imageViewOfertas)


        
    }
    fun obtenerColorFuente(colorTexto: String){
        if (colorTexto.isNotEmpty()){
            Log.e("ColorTexto adapter", colorTexto)

        }


    }
}