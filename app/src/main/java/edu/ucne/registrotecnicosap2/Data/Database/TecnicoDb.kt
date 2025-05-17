package edu.ucne.registrotecnicosap2.Data.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import edu.ucne.registrotecnicosap2.Data.Dao.PrioridadDao
import edu.ucne.registrotecnicosap2.Data.Dao.TecnicoDao
import edu.ucne.registrotecnicosap2.Data.Dao.TicketDao
import edu.ucne.registrotecnicosap2.Data.Entities.PrioridadEntity
import edu.ucne.registrotecnicosap2.Data.Entities.TecnicoEntity
import edu.ucne.registrotecnicosap2.Data.Entities.TicketEntity

@Database(
    entities = [
        TecnicoEntity::class,
        PrioridadEntity::class,
        TicketEntity::class
    ],
    version = 5,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TecnicoDb : RoomDatabase() {
    abstract fun tecnicoDao(): TecnicoDao
    abstract fun prioridadDao(): PrioridadDao
    abstract fun ticketDao(): TicketDao
}