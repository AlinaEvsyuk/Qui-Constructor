package com.evsyuk.quizconstructor.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreateQuestionData(
    var questionIndex: Int = 0,
    val questionStatement: String,
    val option1: String,
    val option2: String? = null,
    val option3: String? = null,
    val option4: String? = null,
    val answer: String,
    val answer2: String? = null
) : Parcelable
