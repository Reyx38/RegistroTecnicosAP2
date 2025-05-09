package edu.ucne.registrotecnicosap2.Data.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.registrotecnicosap2.Data.Dao.TecnicoDao
import edu.ucne.registrotecnicosap2.Data.Entities.TecnicoEntity

@Database(
    entities = [
        TecnicoEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class TecnicoDb : RoomDatabase(){
    abstract fun tecnicoDao(): TecnicoDao
}