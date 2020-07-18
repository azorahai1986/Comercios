package com.example.comercios.repoyviewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.comercios.MainActivity
import com.example.comercios.actividades.ActivityAgregar
import com.example.comercios.modelos.Ofertas
import com.example.comercios.modelos.Promociones

class MainViewModel: ViewModel() {

    val repo = Repo()
    fun fetchUserData(): LiveData<MutableList<Promociones>>{

        val mutableData =MutableLiveData<MutableList<Promociones>>()
        repo.getPromocion().observeForever {
            mutableData.value = it
        }
        return mutableData
    }
    fun fetchUserDataOfertas():LiveData<MutableList<Ofertas>>{
        val mutableDataOfertas = MutableLiveData<MutableList<Ofertas>>()
        repo.getOfertas().observeForever {
            mutableDataOfertas.value = it
        }
        return mutableDataOfertas
    }

}