package com.kader.foxycode_casestudy.Models

data class CategoryModel(
     val en: String? = null,
     val tr: String? = null,
     val words: List<WordModel> = emptyList(),
     val id: String? = null // id ekledim
) {

     private var currentLanguage = "English"

     fun setTitleTurkish() {
          currentLanguage = "Turkish"
     }

     fun setTitleEnglish() {
          currentLanguage = "English"
     }

     fun getCategoryId(): String? {
          return id
     }

}
