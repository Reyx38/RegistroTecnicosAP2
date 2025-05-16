package edu.ucne.registrotecnicosap2.Data.repository

import edu.ucne.registrotecnicosap2.Data.Dao.PrioridadDao
import edu.ucne.registrotecnicosap2.Data.Entities.PrioridadEntity
import kotlinx.coroutines.flow.Flow

class PrioridadRepository (
    private var dao: PrioridadDao
){
    suspend fun save(prioridad: PrioridadEntity) = dao.save(prioridad)
    suspend fun find(id: Int?): PrioridadEntity? = dao.find(id)
    suspend fun  delete(prioridad: PrioridadEntity) = dao.delete(prioridad)
    fun getAll(): Flow<List<PrioridadEntity?>> = dao.getAll()
}