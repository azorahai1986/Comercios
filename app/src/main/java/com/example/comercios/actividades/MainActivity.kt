package com.example.comercios

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.LayoutDirection
import android.util.Log
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import com.example.comercios.actividades.ActivityAgregar
import com.example.comercios.actividades.LoginActivity
import com.example.comercios.actividades.Pdf
import com.example.comercios.adaptadores.AdapterPromociones
import com.example.comercios.adaptadores.OfertasAdapter
import com.example.comercios.modelos.ModeloPdf
import com.example.comercios.modelos.Ofertas
import com.example.comercios.modelos.Promociones
import com.example.comercios.repoyviewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

enum class ProviderType {
    BIENVENIDO
}

class MainActivity : AppCompatActivity() {
    private val viewModel by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java) }

    private var adapter: AdapterPromociones? = null
    private var adapterOfertas: OfertasAdapter? = null
    private lateinit var auth: FirebaseAuth // para saber si hay existe un email
// array para todos los datos que serán enviados al pdf.................
    private var pdfdatosArray: ArrayList<Promociones>?= null
    private var pdfPreciosArray: ArrayList<Double>?= null
    var arrayFiltro: ArrayList<Promociones>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //para saber si existe un email................................
        auth = Firebase.auth
        val user = Firebase.auth.currentUser
        user?.let {
            // Name, email address, and profile photo Url
            val email = user.email

            Log.e("EmailMainAct", email)

            val uid = user.uid

        }
        if (user?.email.isNullOrEmpty()){
            ibCerrarSesion.visibility = View.INVISIBLE
            tvCerrarSesion.visibility = View.INVISIBLE
            flot_btAgregar.visibility = View.INVISIBLE
            tvProvider.visibility = View.INVISIBLE
            tvEmail.visibility = View.INVISIBLE
        }else{


        }
        swiperefreshlayout.setOnRefreshListener {
            //cargar datos de firebase
            loadPromociones()
            swiperefreshlayout.isRefreshing = false
        }
        // traer datos desde la ActividadRegistrarse............................
        val bundle = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")
        setup(email ?: "", provider ?: "")


        // Dar Eventos a los botones.......................................
        btAcceder.setOnClickListener {
            if (user?.email.isNullOrEmpty()){
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(this, "Ya eres Administrador", Toast.LENGTH_SHORT).show()
            }
        }
        ibCerrarSesion.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        //Ir al activityAgregar...............................................
        flot_btAgregar.setOnClickListener {
            val intent = Intent(this, ActivityAgregar::class.java)
            startActivity(intent)
        }
        // inflar recyclerView...................................................
        adapter = AdapterPromociones(arrayListOf(), this)
        rv_promociones.layoutManager = GridLayoutManager(this, 2)
        rv_promociones.adapter = adapter

        adapterOfertas = OfertasAdapter(arrayListOf(), this)
        viewPagerOfertas.adapter = adapterOfertas
        indicator.setViewPager2(viewPagerOfertas)


        // pasaré datos al pdf.......................................................

        ibEnviarAPdf.setOnClickListener {
            if (tvTotPrecio.text.isNullOrEmpty()){
                Toast.makeText(this, "Agrega productos a la lista", Toast.LENGTH_SHORT).show()
            }else{
                // igualaré el array a la los datos que estan en el recycler promociones para enviar esos datos al recyclerPdf
                pdfdatosArray = adapter?.mutableListopromo
                val enviarDatosAlPdf = arrayListOf<ModeloPdf>()
                if (pdfdatosArray != null){
                    for (index in 0 until pdfdatosArray!!.size) {
                        if(pdfPreciosArray!![index] > 0){
                            val productosSeleccionados = ModeloPdf()
                            productosSeleccionados.producto = pdfdatosArray!![index].nombre
                            productosSeleccionados.precio = pdfdatosArray!![index].precio.toDouble()
                            productosSeleccionados.cantidad = ((pdfPreciosArray!![index] / productosSeleccionados.precio).toInt())
                            productosSeleccionados.subTotal = pdfPreciosArray!![index]
                            enviarDatosAlPdf.add(productosSeleccionados)
                        }
                    }

                    val intent = Intent(this, Pdf::class.java)
                    intent.putExtra(Pdf.PROD_SELECT, enviarDatosAlPdf)
                    intent.putExtra("Total Precios", tvTotPrecio.text.toString())
                    startActivity(intent)
                    }
            }
        }
        // dar funcion al editTextSearch para filtrar el recycler:::::::::::::::::::::.....


        edtSearch.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                adapter?.filtrado(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {


            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })


    }
    // Ingresar con mail y contraseña..........................................
    fun setup(email: String, provider:String){
        title = "Inicio"
        tvEmail.text = email
        tvProvider.text = provider

    }
    // Traer los datos de Firebase...............................................
    fun loadPromociones(){
        viewModel.fetchUserData().observe(this, Observer {
            adapter?.setData( it as ArrayList<Promociones>)
            adapter?.notifyDataSetChanged()


        })
    }
    fun loadOfertas(){
        viewModel.fetchUserDataOfertas().observe(this, Observer {
            adapterOfertas?.setDataOfertas(it as ArrayList<Ofertas>)
            adapterOfertas?.notifyDataSetChanged()

          })
    }

    override fun onResume() {
        super.onResume()
        loadPromociones()
        loadOfertas()
    }

    fun obtenerDatosAdapter(arrayCant: ArrayList<Int>, arrayPrec:ArrayList<Double>){
        var sum = 0
        for (i in arrayCant){
            sum += i
            //Log.e("Sumatoria de Main", sum.toString())

        }

        var sumPrecio = 0.0
        for (p in arrayPrec){
            sumPrecio += p
            //Log.e("Sumatoria de Main", sum.toString())

        }
        tvTotPrecio.text = "Total $$sumPrecio"
        pdfPreciosArray = arrayPrec // esto me servirá para enviar los precios totales al pdf
        tvCantidad.text = "$sum Unidades"



    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
    // funcion para el filtrado de recyclerView



}