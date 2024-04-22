package com.evsyuk.quizconstructor.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.evsyuk.quizconstructor.data.model.QuestionsConverter
import com.evsyuk.quizconstructor.data.model.QuizData

@Database(entities = [QuizData::class], version = 1, exportSchema = false)
@TypeConverters(QuestionsConverter::class)
abstract class QuizDatabase : RoomDatabase() {
    companion object {

        private var db: QuizDatabase? = null
        private const val DB_NAME = "quiz.db"
        private val LOCK = Any()

        fun getInstance(context: Context): QuizDatabase {
            synchronized(LOCK) {
                db?.let { return it }
                val instance =
                    Room.databaseBuilder(
                        context,
                        QuizDatabase::class.java,
                        DB_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                db = instance
                return instance
            }
        }
    }

    abstract fun quizDao(): QuizDao
}