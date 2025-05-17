package edu.ucne.registrotecnicosap2.Data.Entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "Tickets",
    foreignKeys = [
        ForeignKey(
            entity = TecnicoEntity::class,
            parentColumns = ["tecnicoId"],
            childColumns = ["tecnicoId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PrioridadEntity::class,
            parentColumns = ["prioridadId"],
            childColumns = ["prioridadId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TicketEntity(
    @PrimaryKey (autoGenerate = true)
    val ticketId: Int? = null,
    val fecha: Date = Date(),
    val prioridadId: Int?,
    val cliente: String = "",
    val asunto: String = "",
    val descripcion: String = "",
    val tecnicoId: Int?
)
