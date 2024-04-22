package com.evsyuk.quizconstructor.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evsyuk.quizconstructor.data.QuizDatabaseRepository
import com.evsyuk.quizconstructor.data.model.QuizData
import com.evsyuk.quizconstructor.utils.DatabaseResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class QuizListViewModel(private val quizDatabaseRepository: QuizDatabaseRepository) : ViewModel() {

    private var _quizList: MutableStateFlow<DatabaseResponse<List<QuizData>>> =
        MutableStateFlow(DatabaseResponse.Loading())
    val quizList get() = _quizList

    fun getQuizByCategory(categoryId: String? = null) {
        viewModelScope.launch {
            _quizList.value = if (categoryId.isNullOrEmpty()) quizDatabaseRepository.quizList() else quizDatabaseRepository.quizListWithCategory(categoryId)
        }
    }
}
