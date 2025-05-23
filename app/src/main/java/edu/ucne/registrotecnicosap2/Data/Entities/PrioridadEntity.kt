package edu.ucne.registrotecnicosap2.Data.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Prioridades")

data class PrioridadEntity (
    @PrimaryKey
    val prioridadId: Int? = null,
    val descripcion: String? = "",
)
