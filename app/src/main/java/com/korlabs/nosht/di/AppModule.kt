package com.korlabs.nosht.di

import android.app.Application
import androidx.room.Room
import com.korlabs.nosht.data.local.NoshtDatabase
import com.korlabs.nosht.data.remote.FirebaseAuthClient
import com.korlabs.nosht.data.remote.FirestoreClient
import com.korlabs.nosht.domain.remote.APIClient
import com.korlabs.nosht.domain.remote.AuthClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebase(): AuthClient = FirebaseAuthClient()

    @Provides
    @Singleton
    fun provideFirestore(): APIClient = FirestoreClient()

    @Provides
    @Singleton
    fun provideNoshtDatabase(app: Application): NoshtDatabase {
        return Room.databaseBuilder(app, NoshtDatabase::class.java, "noshtdb.db")
            .fallbackToDestructiveMigration().build()
    }
}