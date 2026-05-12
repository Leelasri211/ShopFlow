package com.leelasri.commerceappshopflow.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.leelasri.commerceappshopflow.data.local.dao.CartDao
import com.leelasri.commerceappshopflow.data.local.entity.CartEntity

@Database(entities = [CartEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
}