package com.example.comercios.adaptadores

import android.app.AlertDialog
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.comercios.MainActivity
import com.example.comercios.R
import com.example.comercios.actividades.MostrarImagen
import com.example.comercios.modelos.Promociones
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_agregar_al_carrito.view.*
import kotlinx.android.synthetic.main.template_rvpromo.view.*

class AdapterPromociones(var mutableListopromo: ArrayList<Promociones>, val activity: MainActivity)
    :RecyclerView.Adapter<AdapterPromociones.ViewHolderPromo>() {

    // arrays que seran llenados con 0 para que por cada item exista un 0. asi poder contar la cantidad de productos seleccionados. o su precio
    var arrayCantidades: ArrayList<Int> = ArrayList()
    var arrayPecios: ArrayList<Double> = ArrayList()
    var arrayFiltro: ArrayList<Promociones> = ArrayList()

    fun setData(datos: ArrayList<Promociones>){
        mutableListopromo = datos
        Log.e("datos", datos.toString())
        arrayCantidades = ArrayList()
        arrayFiltro = ArrayList(mutableListopromo)
        Log.e("arrayFiltro", arrayFiltro.toString())
        //  promociones [prom1, prom2, prom3]
        // cantidades   [4 ,   2,    0]
        for(d in datos){
            arrayCantidades.add(0)
            arrayPecios.add(0.0)


        }

    }



   inner class ViewHolderPromo(itemView: View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterPromociones.ViewHolderPromo =
        ViewHolderPromo(LayoutInflater.from(parent.context).inflate(R.layout.template_rvpromo, parent, false))

    override fun getItemCount(): Int= arrayFiltro.size


    override fun onBindViewHolder(holder: AdapterPromociones.ViewHolderPromo, position: Int) {

        val enlazarVista = arrayFiltro[position]
        val index = indexPromo(enlazarVista)

        holder.itemView.tvNombre.text = enlazarVista.nombre
        holder.itemView.tvPrecio.text = "$ ${enlazarVista.precio}"
        holder.itemView.tvCateg.text = enlazarVista.cate
        holder.itemView.tviewMarca.text = enlazarVista.marca
        Glide.with(activity).load(enlazarVista.imagen).into(holder.itemView.imageViewPromo)

        holder.itemView.imageViewPromo.setOnClickListener {
            if (activity.tvCantidad.text == "cantidad"){
                activity.startActivity(
                    Intent(activity, MostrarImagen::class.java)
                        .putExtra("Id_Imagen", enlazarVista.imagen)
                        .putExtra("Id_Nombre", enlazarVista.nombre)
                        .putExtra("Id_Marca", enlazarVista.marca)
                        .putExtra("Id", enlazarVista.id)
                        .putExtra("Id_Precio", enlazarVista.precio)
                )
            }else {
                // inflaré el alertDialog
                val dialogCompra = LayoutInflater.from(activity).inflate(R.layout.dialog_agregar_al_carrito, null)
                val dialogConstructor = AlertDialog.Builder(activity)
                    .setView(dialogCompra)
                    .setTitle("  peligro de perder la compra")

                //mostrar el dialog
                val alertDialog = dialogConstructor.show()
                dialogCompra.btQuedarse.setOnClickListener {
                    alertDialog.dismiss()

                }
                dialogCompra.btIr.setOnClickListener {

                   pasarDeActividad(index)
                    alertDialog.dismiss()

                }

            }
        }

        //daré funciones a los botones de agregar y restar.................................
        holder.itemView.tvValorCantidad.text = arrayCantidades[index].toString()
        holder.itemView.tv_template_cantidad.text = arrayCantidades[index].toString()
        holder.itemView.tv_template_precio.text = arrayPecios[index].toString()
        if (arrayCantidades[index] == 0)holder.itemView.tv_template_cantidad.text = "cantidad"
        if (arrayCantidades[index] == 0)holder.itemView.tv_template_precio.text = "precio $"

        // boton para sumar valores............................................
        holder.itemView.btSumar.setOnClickListener {

            arrayCantidades[index] = arrayCantidades[index] + 1

            holder.itemView.tvValorCantidad.text = arrayCantidades[index].toString()
            holder.itemView.tv_template_cantidad.text = "cantidad ${arrayCantidades[index]}"
            arrayPecios[index] = arrayPecios[index] + enlazarVista.precio.toDouble()

            holder.itemView.tv_template_precio.text = " precio $ ${arrayPecios[index]}"

            // desde aquí envio los arrays al main activity. a la funcion obtenerdatosadapter
            activity.obtenerDatosAdapter(arrayCant = arrayCantidades, arrayPrec = arrayPecios)


        }

        // boton para restar los valores.....................................................
        holder.itemView.btRestar.setOnClickListener {
            if (arrayCantidades[index] > 0){
                arrayCantidades[index] = arrayCantidades[index] - 1
                holder.itemView.tvValorCantidad.text = arrayCantidades[index].toString()
                holder.itemView.tv_template_cantidad.text = "cantidad ${arrayCantidades[index]}"
                arrayPecios[index] = arrayPecios[index] - enlazarVista.precio.toDouble()
                holder.itemView.tv_template_precio.text = " precio $ ${arrayPecios[index]}"
                activity.obtenerDatosAdapter(arrayCant = arrayCantidades, arrayPrec = arrayPecios)
            }
        }
    }

    fun indexPromo(prom: Promociones): Int{
        for (i in 0 until mutableListopromo.size){
            if(mutableListopromo[i].id == prom.id)
                return i
            Log.e("return I", i.toString())
        }
        return 0
        Log.e("return 0", 0.toString())
    }

    fun pasarDeActividad(position: Int){
        val enlazarVista = arrayFiltro[position]
        activity.startActivity(
            Intent(activity, MostrarImagen::class.java)
                .putExtra("Id_Imagen", enlazarVista.imagen )
                .putExtra("Id_Nombre", enlazarVista.nombre)
                .putExtra("Id_Marca", enlazarVista.marca)
                .putExtra("Id", enlazarVista.id)
                .putExtra("Id_Precio", enlazarVista.precio))
    }

    fun filtrado(editFiltro: String){
        if (editFiltro.isNotEmpty()) {
            arrayFiltro = ArrayList()
            for (d in mutableListopromo) {
                if (editFiltro in d.nombre) {
                    arrayFiltro.add(d)
                }
            }
        } else{
            arrayFiltro = ArrayList(mutableListopromo)
        }
        notifyDataSetChanged()

    }



}