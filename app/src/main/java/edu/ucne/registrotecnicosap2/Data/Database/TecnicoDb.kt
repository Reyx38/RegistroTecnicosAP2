package edu.ucne.registrotecnicosap2.Data.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.registrotecnicosap2.Data.Dao.TecnicoDao
import edu.ucne.registrotecnicosap2.Data.Entities.PrioridadEntity
import edu.ucne.registrotecnicosap2.Data.Entities.TecnicoEntity
import edu.ucne.registrotecnicosap2.Data.Entities.TicketEntity

@Database(
    entities = [
        TecnicoEntity::class,
        PrioridadEntity::class,
        TicketEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class TecnicoDb : RoomDatabase() {
    abstract fun tecnicoDao(): TecnicoDao
}