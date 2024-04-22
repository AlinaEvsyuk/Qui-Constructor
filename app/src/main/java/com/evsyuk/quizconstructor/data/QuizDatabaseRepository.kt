package com.evsyuk.quizconstructor.data

import android.content.Context
import com.evsyuk.quizconstructor.data.database.QuizDao
import com.evsyuk.quizconstructor.data.database.QuizDatabase
import com.evsyuk.quizconstructor.data.model.QuizData
import com.evsyuk.quizconstructor.utils.DatabaseResponse

class QuizDatabaseRepository(context: Context) {

    private var quizDao : QuizDao

    private var db : QuizDatabase = QuizDatabase.getInstance(context)

    init {
        this.quizDao = db.quizDao()
    }

    suspend fun uploadQuiz(quizData: QuizData): DatabaseResponse<String> {
        quizDao.uploadQuiz(quizData)
        return DatabaseResponse.Success(data = null, message = "Added")
    }

    suspend fun quizList(): DatabaseResponse<List<QuizData>> {
        val quizList = quizDao.getQuizList()
        return DatabaseResponse.Success(data = quizList, message = "Success")
    }

    suspend fun quizListWithCategory(id: String): DatabaseResponse<List<QuizData>> {
        val quizListWithCategory = quizDao.getQuizListWithCategory(id)
        return DatabaseResponse.Success(data = quizListWithCategory, message = "Success")
    }
}