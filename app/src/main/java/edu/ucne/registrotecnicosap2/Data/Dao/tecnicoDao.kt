package edu.ucne.registrotecnicosap2.Data.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.registrotecnicosap2.Data.Entities.tecnicoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface tecnicoDao {
    @Upsert()
    suspend fun save (tecnico: tecnicoEntity)
    @Query(
        """
        SELECT * 
        FROM Tecnicos 
        WHERE tecnicoId=:id  
        LIMIT 1
        """
    )
    suspend fun find(id: Int): tecnicoEntity?
    @Delete
    suspend fun delete(tecnico: tecnicoEntity)
    @Query("SELECT * FROM Tecnicos")
    fun getAll(): Flow<List<tecnicoEntity>>

}