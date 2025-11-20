package com.example.revicar_rgi.data.remote

import com.example.revicar_rgi.data.model.Marca
import com.example.revicar_rgi.data.model.Modelo
import retrofit2.http.GET
import retrofit2.http.Path

interface VehicleApiService {
    @GET("/api/marcas")
    suspend fun getMarcas(): List<Marca>

    @GET("/api/modelos/marca/{marcaId}")
    suspend fun getModelosByMarca(@Path("marcaId") marcaId: Int): List<Modelo>
}

