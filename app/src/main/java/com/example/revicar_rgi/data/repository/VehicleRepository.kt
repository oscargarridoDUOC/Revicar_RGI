package com.example.revicar_rgi.data.repository

import com.example.revicar_rgi.data.model.Marca
import com.example.revicar_rgi.data.model.Modelo
import com.example.revicar_rgi.data.remote.RetrofitInstance

interface VehicleRepositoryInterface {
    suspend fun getMarcas(): List<Marca>
    suspend fun getModelosByMarca(marcaId: Int): List<Modelo>
}

class VehicleRepository : VehicleRepositoryInterface {
    override suspend fun getMarcas(): List<Marca> {
        return RetrofitInstance.vehicleApi.getMarcas()
    }

    override suspend fun getModelosByMarca(marcaId: Int): List<Modelo> {
        return RetrofitInstance.vehicleApi.getModelosByMarca(marcaId)
    }
}

