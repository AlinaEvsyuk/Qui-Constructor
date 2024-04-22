package com.evsyuk.quizconstructor.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuizResultModel(
    val question: String,
    val givenAns: String,
    val correctAns: String,
    val isCorrect: Boolean
) : Parcelable
