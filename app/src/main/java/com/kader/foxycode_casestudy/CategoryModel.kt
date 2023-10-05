package com.kader.foxycode_casestudy

data class CategoryModel(
     val en: String? = null,
     val tr: String? = null,
     val words: List<WordModel> = listOf()
)