package edu.ucne.registrotecnicosap2.Data.repository

import edu.ucne.registrotecnicosap2.Data.Dao.TecnicoDao
import edu.ucne.registrotecnicosap2.Data.Entities.TecnicoEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TecnicoRepository @Inject constructor(
    private val dao: TecnicoDao
) {
    suspend fun save(tecnico: TecnicoEntity) = dao.save(tecnico)
    suspend fun find(id: Int): TecnicoEntity? = dao.find(id)
    suspend fun  delete(tecnico: TecnicoEntity) = dao.delete(tecnico)
    fun getAll(): Flow<List<TecnicoEntity>> = dao.getAll()
}