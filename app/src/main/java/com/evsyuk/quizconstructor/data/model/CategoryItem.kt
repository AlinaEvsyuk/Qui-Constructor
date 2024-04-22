package com.evsyuk.quizconstructor.data.model

data class CategoryItem(
    val categoryId: String,
    val categoryName: String
){
    override fun toString(): String {
        return categoryName
    }
}
