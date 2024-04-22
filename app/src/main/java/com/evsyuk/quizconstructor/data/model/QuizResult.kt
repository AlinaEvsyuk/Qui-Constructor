package com.evsyuk.quizconstructor.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuizResult(
    val totalQuestion: Int,
    val rightAnswer: Int,
    val wrongAnswer: Int
) : Parcelable
