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
import kotlinx.android.synthetic.main.activity_main.*
enum class ProviderType {
    BIENVENIDO
}

class MainActivity : AppCompatActivity() {
    private val viewModel by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java) }

    private var adapter: AdapterPromociones? = null
    var datosDelAdapter:ArrayList<String>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        swiperefreshlayout.setOnRefreshListener {
            //cargar datos de firebase
            loadPromociones()
            swiperefreshlayout.isRefreshing = false
        }
        // traer datos desde la ActividadRegistrarse............................
        val bundle = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")
        val token = bundle?.getString("idToken")
        setup(email ?: "", provider ?: "", token ?: "")

        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "Visitante", Toast.LENGTH_SHORT).show()
            flot_btAgregar.visibility = View.GONE
        } else {
            Toast.makeText(this, "Administrador", Toast.LENGTH_SHORT).show()
            Log.e("TokenMainAct", token.withIndex().toString())
            ibCerrarSesion.visibility = View.VISIBLE
            tvCerrarSesion.visibility = View.VISIBLE
            tvTitulo.visibility = View.GONE
            flot_btAgregar.visibility = View.VISIBLE
        }
        // Dar Eventos a los botones.......................................
        btAcceder.setOnClickListener {
            if (token.isNullOrEmpty()){
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
            val b = Bundle()
            b.putString("datosDelActivity", datosDelAdapter.toString())
            intent.putExtras(b)
            //Log.e("datosDelAdapter", datosDelAdapter)
            startActivity(intent)
        }
        // inflar recyclerView...................................................
        adapter = AdapterPromociones(arrayListOf(), this)
        rv_promociones.layoutManager = GridLayoutManager(this, 2)
        rv_promociones.adapter = adapter





        loadPromociones()

    }
    // Ingresar con mail y contraseña..........................................
    fun setup(email: String, provider:String, token:String){
        title = "Inicio"
        tvEmail.text = email
        tvProvider.text = provider
        val token = token

    }
    // Traer los datos de Firebase...............................................
    fun loadPromociones(){
        viewModel.fetchUserData().observe(this, Observer {
            adapter?.mutableListopromo = it as ArrayList<Promociones>
            adapter?.notifyDataSetChanged()

        })
    }



}