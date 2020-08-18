package com.example.comercios.actividades

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProviders
import com.example.comercios.R
import com.example.comercios.adaptadores.AdapterPromociones
import com.example.comercios.adaptadores.OfertasAdapter
import com.example.comercios.modelos.Ofertas
import com.example.comercios.modelos.Promociones
import com.example.comercios.repoyviewmodel.MainViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_agregar.*
import kotlinx.android.synthetic.main.activity_crear_ofertas.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class CrearOfertas : AppCompatActivity(), View.OnClickListener {

    private val PICK_IMAGE_REQUEST = 1234
    private val viewModel by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java) }
    private var adapter: OfertasAdapter? = null

    // Método para subir imagenes al firebase storage
    private var filePath: Uri? = null
    private var storageReference: StorageReference? = null
    override fun onClick(v: View?) {
        if (v === image_crar_ofertas)
            showFilerChooser()
        else(v === bt_cargar_ofertas)
        uploadFile()

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null){
            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                image_crar_ofertas!!.setImageBitmap(bitmap)
            }catch (e: IOException){
                e.printStackTrace()
            }
        }

    }

    private fun uploadFile() {
        if (filePath != null){
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Cargando...")
            progressDialog.show()

            // para modificar los datos de una lista usando firestore..........................

            val imageRef = storageReference!!.child("images/"+ UUID.randomUUID().toString())

            var uploadTask = imageRef.putFile(filePath!!)
            val urlTask = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                imageRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result

                    // para editar los datos de la lista...........................................

                    var precio = edit_text_precio_ofertas.text.toString()
                    var imagen = downloadUri.toString()

                    var map = mutableMapOf<String,Any>()
                    // map["id"] = id
                    map["precioOferta"] = precio
                    map["imagen"] = imagen


                    FirebaseFirestore.getInstance().collection("Ofertas")
                        .document().set(map)
                    finish()
                } else {
                    // Handle failures
                    // ...
                }

            }

        }



    }

    private fun showFilerChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "SELECT PICTURE"), PICK_IMAGE_REQUEST)

    }
    lateinit var storage: FirebaseStorage
    var isOpen = true // para animar los botones

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_ofertas)

        //para animar los botones
        val abrire = AnimationUtils.loadAnimation(this, R.anim.abrir)
        val cerrar = AnimationUtils.loadAnimation(this, R.anim.cerrar)
        bt_cargar_colores.setOnClickListener {

            isOpen = if (isOpen){
                bt_color_negro.startAnimation(abrire)
                bt_color_naranja.startAnimation(abrire)
                bt_color_azul.startAnimation(abrire)
                text_view_color_fuente.startAnimation(cerrar)
                false
            }else{
                bt_color_negro.startAnimation(cerrar)
                bt_color_naranja.startAnimation(cerrar)
                bt_color_azul.startAnimation(cerrar)
                text_view_color_fuente.startAnimation(abrire)
                true
            }
        }

        var colorFuente = ""
        bt_color_negro.setOnClickListener {
            if (!isOpen) {
                text_view_precio_oferta.setTextColor(getColor(R.color.common_google_signin_btn_text_dark_focused))
                colorFuente = "negro"
                text_view_Informar_color.text = colorFuente
                Log.e("ColorTextView", text_view_Informar_color.text.toString())
                adapter?.obtenerColorFuente(text_view_Informar_color.toString())

            }

        }

        bt_color_azul.setOnClickListener {
           if (!isOpen){
               text_view_precio_oferta.setTextColor(getColor(R.color.colorPrimary))
               colorFuente = "azul"
               text_view_Informar_color.text = colorFuente
               Log.e("ColorTextView", text_view_Informar_color.text.toString())
               adapter?.obtenerColorFuente(text_view_Informar_color.text.toString())

           }

        }

        bt_color_naranja.setOnClickListener {
            if (!isOpen) {
                text_view_precio_oferta.setTextColor(getColor(R.color.colorFirebase))
                colorFuente = "naranja"
                text_view_Informar_color.text = colorFuente
                Log.e("ColorTextView", text_view_Informar_color.text.toString())
                adapter?.obtenerColorFuente(colorFuente)

            }


        }



        storage = Firebase.storage

        // dare init a firebase
        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference

        image_crar_ofertas.setOnClickListener(this)
        bt_cargar_ofertas.setOnClickListener(this)

        exTraerDatos()
    }

    fun exTraerDatos(){
        viewModel.fetchUserDataOfertas().observe(this, androidx.lifecycle.Observer {
            adapter?.itemsOfertas = it as ArrayList<Ofertas>
            adapter?.notifyDataSetChanged()
        })
    }

}