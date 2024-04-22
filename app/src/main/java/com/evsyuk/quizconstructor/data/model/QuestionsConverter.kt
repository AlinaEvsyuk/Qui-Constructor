package com.evsyuk.quizconstructor.data.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class QuestionsConverter {

    @TypeConverter
    fun stringToSrc(value: String): List<CreateQuestionData> {
        val srcType: Type = object : TypeToken<List<CreateQuestionData>?>() {}.type
        return Gson().fromJson(value, srcType)
    }

    @TypeConverter
    fun srcToString(src: List<CreateQuestionData>): String {
        val gson = Gson()
        return gson.toJson(src)
    }

}