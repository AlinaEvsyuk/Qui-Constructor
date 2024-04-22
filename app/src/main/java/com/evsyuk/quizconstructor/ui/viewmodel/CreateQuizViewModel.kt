package com.evsyuk.quizconstructor.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evsyuk.quizconstructor.data.QuizDatabaseRepository
import com.evsyuk.quizconstructor.data.database.QuizDatabase
import com.evsyuk.quizconstructor.data.model.QuizData
import com.evsyuk.quizconstructor.data.model.CreateQuestionData
import com.evsyuk.quizconstructor.utils.DatabaseResponse
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CreateQuizViewModel(private val quizDatabaseRepository: QuizDatabaseRepository): ViewModel() {

    private var _uploadResponse: MutableStateFlow<DatabaseResponse<String>> =
        MutableStateFlow(DatabaseResponse.Loading())
    val uploadResponse get() = _uploadResponse

    private val _questionList = MutableStateFlow<List<CreateQuestionData>>(mutableListOf())
    val questionList = _questionList.asStateFlow()

    var index = 0

    fun addQuestion(createQuestionData: CreateQuestionData) = viewModelScope.launch {
        val list = questionList.value.toMutableList()
        createQuestionData.questionIndex = index++
        list.add(createQuestionData)
        _questionList.emit(list)
    }

    fun removeQuestion(index: Int) = viewModelScope.launch {
        val list = questionList.value.toMutableList()
        list.removeAt(index)
        _questionList.emit(list)
    }

    fun uploadQuiz(quizData: QuizData) {
        viewModelScope.launch(IO) {
            _uploadResponse.value = quizDatabaseRepository.uploadQuiz(quizData)
        }
    }
}
