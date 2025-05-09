package edu.ucne.registrotecnicosap2.Data.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.registrotecnicosap2.Data.Dao.tecnicoDao
import edu.ucne.registrotecnicosap2.Data.Entities.tecnicoEntity

@Database(
    entities = [
        tecnicoEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class tecnicoDb : RoomDatabase(){
    abstract fun tecnicoDao(): tecnicoDao
}