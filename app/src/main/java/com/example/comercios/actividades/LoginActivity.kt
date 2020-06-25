package com.example.comercios.actividades

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.example.comercios.MainActivity
import com.example.comercios.ProviderType
import com.example.comercios.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.btAcceder

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setup()
    }
    private fun setup(){
        btIngresar?.setOnClickListener {


            if (etEmail.text.isNotEmpty() && etPassword.text.isNotEmpty()) {

                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(etEmail.text.toString(), etPassword.text.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            showHome(
                                it.result?.user?.email ?: "",
                                ProviderType.BIENVENIDO,
                                idToken = "idToken"
                            )

                        } else {
                            showAlertAcceder()

                        }

                    }

            }
            val mUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
            mUser?.getIdToken(true)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        val task = task?.result?.token ?: ""
                        Log.e("TaskLoginAct", task)


                    } else {
                        // Handle error -> task.getException();
                    }
                }

        }


    }
        fun showAlertAcceder(){
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Error")
            builder.setMessage(" El Email ó la Contraseña son erroneos")
            builder.setPositiveButton("Aceptar", null)
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
        // funcion para pasar los datos del email y la contraseña
        // este providerType será creado en el activityEditor
        private fun showHome(email: String, provider: ProviderType, idToken: String){
            val homeIntent = Intent(this, MainActivity::class.java)
            homeIntent.putExtra("email", email)
            homeIntent.putExtra("provider", provider.name)
            homeIntent.putExtra("idToken", idToken)

            startActivity(homeIntent)


        }


}