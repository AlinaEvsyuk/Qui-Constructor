package com.evsyuk.quizconstructor.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.evsyuk.quizconstructor.data.model.QuizData

@Dao
interface QuizDao {

    @Query("SELECT * FROM quiz_list")
     fun getQuizList(): List<QuizData>

    @Query("SELECT * FROM quiz_list WHERE categoryId == :id")
     fun getQuizListWithCategory(id: String): List<QuizData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun uploadQuiz(quizData: QuizData)
}