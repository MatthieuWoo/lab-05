package com.example.listycity

import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class CityRepository {

    private val db = Firebase.firestore
    private val citiesRef = db.collection("cities")

    private val _cities = mutableStateListOf<City>()

    val cities: List<City>
        get() = _cities

    init {
        citiesRef.addSnapshotListener { snapshots, exception ->
            if (exception != null) {
                return@addSnapshotListener
            }
            _cities.clear()
            snapshots?.documents?.forEach { document ->
                val city = document.toObject(City::class.java)
                if (city != null) {
                    _cities.add(city)
                }
            }
        }
    }

    fun addCity(city: City) {
        if (city.name.isNotBlank()) {
            citiesRef.document(city.name).set(city)
        }
    }

    fun updateCity(oldCity: City, updatedCity: City) {
        if (oldCity.name.isNotBlank() && updatedCity.name.isNotBlank()) {
            if (oldCity.name != updatedCity.name) {
                citiesRef.document(oldCity.name).delete()
            }
            citiesRef.document(updatedCity.name).set(updatedCity)
        }
    }

    fun deleteCity(city: City) {
        if (city.name.isNotBlank()) {
            citiesRef.document(city.name).delete()
        }
    }
}