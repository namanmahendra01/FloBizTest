package com.naman.flobizstack.DBroom

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.naman.flobizstack.models.Item

@Database(entities = [Item::class],version = 1)
@TypeConverters(Converters::class)
abstract class QuestionDatabase:RoomDatabase() {


abstract  fun getQuestionDao(): QuestionDao

companion object{
    @Volatile
    private var instance: QuestionDatabase?=null

    private val LOCK=Any()

    operator fun invoke (context: Context)= instance ?: synchronized(LOCK){
        instance ?: createDatabase(context).also {
            instance =it
        }
    }


        private fun createDatabase(context:Context)=
        Room.databaseBuilder(
        context.applicationContext,
        QuestionDatabase::class.java,
        "question_db.db"


    ).build()


}
}