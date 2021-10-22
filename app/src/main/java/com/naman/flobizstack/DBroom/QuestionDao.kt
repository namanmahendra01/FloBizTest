package com.naman.flobizstack.DBroom

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.naman.flobizstack.models.Item

@Dao
interface QuestionDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(item: Item):Long

    @Query("Select * from questions")
    fun getItem(): LiveData<List<Item>>

    @Delete
    suspend fun deleteArticle(article: Item)
}