package com.example.comercios.adaptadores

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.comercios.MainActivity
import com.example.comercios.R
import com.example.comercios.actividades.MostrarImagen
import com.example.comercios.modelos.Promociones
import kotlinx.android.synthetic.main.template_rvpromo.view.*

class AdapterPromociones(var mutableListopromo: ArrayList<Promociones>, val activity: MainActivity):RecyclerView.Adapter<AdapterPromociones.ViewHolderPromo>() {


    var arrayDatos = ArrayList<String>(mutableListopromo.size)
    fun darValor(items:ArrayList<Promociones>){
        mutableListopromo = items

        arrayDatos = ArrayList()
        Log.e("MutabledaAdapter2", mutableListopromo.toString())

        var sum = ""
        for (x in items){
            Log.e("AdapFor", x.nombre)
            arrayDatos.add(x.nombre)
            sum += x.nombre
            val text = sum

            Log.e("Adapfor1", arrayDatos.toString())
            Log.e("Adapfor2", text)
        }
       // activity.extraerDatos(nombres = arrayDatos)

    }


   inner class ViewHolderPromo(itemView: View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterPromociones.ViewHolderPromo =
        ViewHolderPromo(LayoutInflater.from(parent.context).inflate(R.layout.template_rvpromo, parent, false))

    override fun getItemCount(): Int= mutableListopromo.size


    override fun onBindViewHolder(holder: AdapterPromociones.ViewHolderPromo, position: Int) {
        val enlazarVista =mutableListopromo[position]

        holder.itemView.tvNombre.text = enlazarVista.nombre
        holder.itemView.tvPrecio.text = enlazarVista.precio
        holder.itemView.tvCateg.text = enlazarVista.cate
        holder.itemView.tviewMarca.text = enlazarVista.marca
        Glide.with(activity).load(enlazarVista.imagen).into(holder.itemView.imageViewPromo)

        holder.itemView.cardView_promo.setOnClickListener {
            activity.startActivity(Intent(activity, MostrarImagen::class.java)
                .putExtra("Id_Imagen", enlazarVista.imagen)
                .putExtra("Id_Nombre", enlazarVista.nombre)
                .putExtra("Id_Marca", enlazarVista.marca)
                .putExtra("Id", enlazarVista.id)
                .putExtra("Id_Precio", enlazarVista.precio))
        }


    }

}