package edu.ucne.registrotecnicosap2.Data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.ucne.registrotecnicosap2.Data.Database.TecnicoDb
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    @Singleton
    fun provideTecnicoBd(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(
            appContext,
            TecnicoDb::class.java,
            "Tecnico.db"
        ).fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideTecnicoDao(tecnicoDb: TecnicoDb) = tecnicoDb.tecnicoDao()
    @Provides
    fun providePrioridaDao(tecnicoDb: TecnicoDb) = tecnicoDb.prioridadDao()
    @Provides
    fun  provideTicketDao(tecnicoDb: TecnicoDb) = tecnicoDb.ticketDao()
}