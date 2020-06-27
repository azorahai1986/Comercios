package com.example.comercios

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.comercios.actividades.ActivityAgregar
import com.example.comercios.actividades.LoginActivity
import com.example.comercios.adaptadores.AdapterPromociones
import com.example.comercios.modelos.Promociones
import com.example.comercios.repoyviewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_mostrar_imagen.*

enum class ProviderType {
    BIENVENIDO
}

class MainActivity : AppCompatActivity() {
    private val viewModel by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java) }

    private var adapter: AdapterPromociones? = null
    private lateinit var auth: FirebaseAuth // para saber si hay existe un email



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
            tvTitulo.visibility = View.VISIBLE
            flot_btAgregar.visibility = View.INVISIBLE
            tvProvider.visibility = View.INVISIBLE
            tvEmail.visibility = View.INVISIBLE
        }else{

            tvTitulo.visibility = View.INVISIBLE

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
            adapter?.mutableListopromo = it as ArrayList<Promociones>
            adapter?.notifyDataSetChanged()

        })
    }

    override fun onResume() {
        super.onResume()
        loadPromociones()
    }


}