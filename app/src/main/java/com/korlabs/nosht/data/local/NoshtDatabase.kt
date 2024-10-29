package com.korlabs.nosht.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.korlabs.nosht.data.local.entities.BusinessEntity
import com.korlabs.nosht.data.local.entities.ContractEntity
import com.korlabs.nosht.data.local.entities.EmployerEntity
import com.korlabs.nosht.data.local.entities.MenuEntity
import com.korlabs.nosht.data.local.entities.MenuResourceCrossRefEntity
import com.korlabs.nosht.data.local.entities.ResourceEntity
import com.korlabs.nosht.data.local.entities.TableEntity

@Database(
    entities = [
        BusinessEntity::class,
        TableEntity::class,
        EmployerEntity::class,
        ContractEntity::class,
        ResourceEntity::class,
        MenuEntity::class,
        MenuResourceCrossRefEntity::class
    ],
    version = 9
)
abstract class NoshtDatabase : RoomDatabase() {
    abstract val dao: NoshtDao
}