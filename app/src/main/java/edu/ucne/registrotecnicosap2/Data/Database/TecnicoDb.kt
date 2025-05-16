package edu.ucne.registrotecnicosap2.Data.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.registrotecnicosap2.Data.Dao.TecnicoDao
import edu.ucne.registrotecnicosap2.Data.Entities.PrioridadEntity
import edu.ucne.registrotecnicosap2.Data.Entities.TecnicoEntity

@Database(
    entities = [
        TecnicoEntity::class,
        PrioridadEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class TecnicoDb : RoomDatabase() {
    abstract fun tecnicoDao(): TecnicoDao
}