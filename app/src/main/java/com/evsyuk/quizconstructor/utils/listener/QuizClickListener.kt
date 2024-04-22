package com.evsyuk.quizconstructor.utils.listener

import com.evsyuk.quizconstructor.data.model.QuizData

interface QuizClickListener {
    fun quizClickListener(quizData: QuizData)
}
