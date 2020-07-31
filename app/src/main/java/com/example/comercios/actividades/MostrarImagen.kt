package com.example.comercios.actividades

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.marginTop
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.comercios.MainActivity
import com.example.comercios.R
import com.example.comercios.modelos.Promociones
import com.example.comercios.repoyviewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_agregar.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_mostrar_imagen.*
import kotlinx.android.synthetic.main.template_rvpromo.view.*

class MostrarImagen : AppCompatActivity() {
    //private val viewModel by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java) }

    var isOpen = false
    var infoEmail:String? = null
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
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mostrar_imagen)

        val rotate = AnimationUtils.loadAnimation(this, R.anim.rotar)
        val abrire = AnimationUtils.loadAnimation(this, R.anim.abrir)
        auth = Firebase.auth
        val user = Firebase.auth.currentUser
        user?.let {
            val email = user.email
            val uid = user.uid
        }

            if (user?.email.isNullOrEmpty()){
                toolbarMostrar.visibility = View.GONE

            }

        imagebtEditar.setOnClickListener {

            if (isOpen){

                Toast.makeText(this, "ya estas editando", Toast.LENGTH_SHORT).show()
                isOpen = false
            }else{
                floatBtEditProducto.startAnimation(abrire)
                floatBtEditProducto.startAnimation(rotate)
                floatBtEditPrecio.startAnimation(abrire)
                floatBtEditPrecio.startAnimation(rotate)
                floatBtEditMarca.startAnimation(abrire)
                floatBtEditMarca.startAnimation(rotate)
                imagebtEditar.startAnimation(rotate)
                isOpen = true

            }
            tvEditMostrar.visibility = View.VISIBLE
            tvDeleteMostrar.visibility = View.VISIBLE
            etMostrarNombre.visibility = View.VISIBLE
            etMostrarMarca.visibility = View.VISIBLE
            etMostrarPrecio.visibility = View.VISIBLE
            floatBtEditProducto.visibility = View.VISIBLE
            floatBtEditMarca.visibility = View.VISIBLE
            floatBtEditPrecio.visibility = View.VISIBLE
            tvSalirEdicion.visibility = View.VISIBLE
            
        }


        // metodo para editar y eliminar lista
        idImagen = intent.getStringExtra(ID_IMAGEN)
        idPrecio = intent.getStringExtra(ID_PRECIO)
        idNombre = intent.getStringExtra(ID_NOMBRE)
        idMarca = intent.getStringExtra(ID_MARCA)
        id = intent.getStringExtra(ID)

        tvMostrarPrecio.text = "$ $idPrecio"
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

            var precio = etMostrarPrecio.text.toString()
            var map = mutableMapOf<String,Any>()
            map["precio"] = precio

            val editar = FirebaseFirestore.getInstance().collection("Promociones").document(id.toString())
            editar.update(map)
                .addOnSuccessListener {
                    Toast.makeText(this, "Precio Modificado con exito", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Falló Modificación", Toast.LENGTH_SHORT).show()

                }

        }
        floatBtEditMarca.setOnClickListener {
            var marca = etMostrarMarca.text.toString()
            var map = mutableMapOf<String,Any>()
            map["marca"] = marca
            val editar = FirebaseFirestore.getInstance().collection("Promociones").document(id.toString())
            editar.update(map)
                .addOnSuccessListener {
                    Toast.makeText(this, "Marca Modificada con exito", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Falló Modificación", Toast.LENGTH_SHORT).show()

                }

        }
        floatBtEditProducto.setOnClickListener {
            var nombre = etMostrarNombre.text.toString()
            var map = mutableMapOf<String,Any>()
            map["nombre"] = nombre
            val editar = FirebaseFirestore.getInstance().collection("Promociones").document(id.toString())
            editar.update(map)
                .addOnSuccessListener {
                    Toast.makeText(this, "Producto Modificado con exito", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Falló Modificación", Toast.LENGTH_SHORT).show()

                }
        }
        tvSalirEdicion.setOnClickListener {
           finish()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
