package com.evsyuk.quizconstructor.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "quiz_list")
data class QuizData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val quizTitle: String,
    val categoryId: String,
    val categoryName: String,
    val questionList: List<CreateQuestionData>,
    val totalQuestion: Int
) : Parcelable
