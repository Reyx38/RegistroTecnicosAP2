package edu.ucne.registrotecnicosap2.Data.repository

import edu.ucne.registrotecnicosap2.Data.Dao.TicketDao
import edu.ucne.registrotecnicosap2.Data.Entities.TecnicoEntity
import edu.ucne.registrotecnicosap2.Data.Entities.TicketEntity
import kotlinx.coroutines.flow.Flow

class TicketRepository(
    private val dao : TicketDao
) {
    suspend fun save(ticket: TicketEntity) = dao.save(ticket)
    suspend fun find(id: Int?): TicketEntity? = dao.find(id)
    suspend fun delete(ticket: TicketEntity) = dao.delete(ticket)
    fun getAll(): Flow<List<TicketEntity?>> = dao.getAll()
}