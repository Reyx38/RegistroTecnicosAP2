package edu.ucne.registrotecnicosap2.Data.Entities
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Tecnicos")
data class TecnicoEntity (
    @PrimaryKey
    val tecnicoId: Int? = null,
    val nombre: String = "",
    val sueldoHora: Float = 0.0F
)