package com.example.comercios.actividades

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.comercios.MainActivity
import com.example.comercios.R
import com.example.comercios.modelos.Promociones
import com.example.comercios.repoyviewmodel.MainViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_agregar.*
import kotlinx.android.synthetic.main.activity_mostrar_imagen.*
import kotlinx.android.synthetic.main.template_rvpromo.view.*

class MostrarImagen : AppCompatActivity() {
    //private val viewModel by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java) }

    var idImagen:String?=null
    var idPrecio:String?=null
    var idNombre:String?=null
    var idMarca:String?=null
    var id:String?=null
    companion object{
        const val ID_IMAGEN = "Id_Imagen"
        const val ID_PRECIO = "Id_Precio"
        const val ID_NOMBRE = "Id_Nombre"
        const val ID_MARCA = "Id_Marca"
        const val ID = "Id"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mostrar_imagen)

        idImagen = intent.getStringExtra(ID_IMAGEN)
        idPrecio = intent.getStringExtra(ID_PRECIO)
        idNombre = intent.getStringExtra(ID_NOMBRE)
        idMarca = intent.getStringExtra(ID_MARCA)
        Log.e("Precio", idPrecio)
        id = intent.getStringExtra(ID)

        tvMostrarPrecio.text = idPrecio
        tvMostrarNombre.text = idNombre
        tvMostrarMarca.text = idMarca
        Glide.with(applicationContext).load(idImagen).into(iV_mostrarImagen)
        tvMostrarNombre.setOnClickListener {
            tvMostrarNombre.visibility = View.INVISIBLE
        }

        imagebtDelete.setOnClickListener {
            FirebaseFirestore.getInstance().collection("Promociones").document(id.toString())
                .delete().addOnSuccessListener {
                    Toast.makeText(this, "Archivo Eliminado", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Falló", Toast.LENGTH_SHORT).show()

                }

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        floatBtEditPrecio.setOnClickListener {

            tvMostrarPrecio.text = etMostrarPrecio.text
            val editar = FirebaseFirestore.getInstance().collection("Promociones").document("id")
            editar.update("precio", tvMostrarPrecio )
                .addOnSuccessListener {
                    Toast.makeText(this, "Precio Modificado con exito", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Falló Modificación", Toast.LENGTH_SHORT).show()

                }

        }

    }
}