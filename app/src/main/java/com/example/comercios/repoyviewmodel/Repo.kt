package com.example.comercios.repoyviewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.comercios.actividades.ActivityAgregar
import com.example.comercios.modelos.Ofertas
import com.example.comercios.modelos.Promociones
import com.google.firebase.firestore.FirebaseFirestore

class Repo {

    fun getPromocion(): LiveData<MutableList<Promociones>>{

        val mutableDatabase = MutableLiveData<MutableList<Promociones>>()        //no voy a depender de ninguna categoria
        FirebaseFirestore.getInstance().collection("Promociones").get()
            .addOnSuccessListener {
                val listData = mutableListOf<Promociones>()

                for (promo in it.documents){
                    val p = promo.toObject(Promociones::class.java)
                    p?.id = promo.id
                    if (p != null){

                        listData.add(p)
                        //Log.e("Repo1", p.toString())
                    }
                }
                mutableDatabase.value = listData

                //Log.e("RepoMutabledata", listData.toString())

            }.addOnFailureListener {Log.e("ErrorMODELO", it.toString())}
        return mutableDatabase



    }
    fun getOfertas():LiveData<MutableList<Ofertas>>{
        val mutableDatabaseOfertas = MutableLiveData<MutableList<Ofertas>>()
        FirebaseFirestore.getInstance().collection("Ofertas").get()
            .addOnSuccessListener {
                val listDataOfertas = mutableListOf<Ofertas>()

                for (ofertas in it.documents){
                    val o = ofertas.toObject(Ofertas::class.java)
                    o?.id = ofertas.id
                    if (o != null){
                        listDataOfertas.add(o)
                    }
                }
                mutableDatabaseOfertas.value = listDataOfertas
            }.addOnFailureListener { Log.e("ErrorMODELO", it.toString()) }
        return mutableDatabaseOfertas
    }
}