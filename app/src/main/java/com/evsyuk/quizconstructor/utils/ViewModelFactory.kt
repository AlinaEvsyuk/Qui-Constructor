package com.evsyuk.quizconstructor.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.evsyuk.quizconstructor.data.QuizDatabaseRepository

class ViewModelFactory(
    private val quizDatabaseRepository: QuizDatabaseRepository
) : ViewModelProvider.Factory  {

//    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
//        return CreateQuizViewModel(quizDatabaseRepository) as T
//    }
//
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return QuizListViewModel(quizDatabaseRepository) as T
//    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(QuizDatabaseRepository::class.java).newInstance(quizDatabaseRepository)
    }
}