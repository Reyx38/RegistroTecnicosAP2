package edu.ucne.registrotecnicosap2.Data.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.registrotecnicosap2.Data.Entities.PrioridadEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PrioridadDao {
    @Upsert
    suspend fun save (prioridad: PrioridadEntity)
    @Query(
        """
        SELECT * 
        FROM Prioridades 
        WHERE prioridadId=:id  
        LIMIT 1
        """
    )
    suspend fun find(id: Int?):PrioridadEntity?
    @Delete
    suspend fun delete (prioridad: PrioridadEntity)
    @Query("Select * from Prioridades")
    fun getAll(): Flow<List<PrioridadEntity?>>
}