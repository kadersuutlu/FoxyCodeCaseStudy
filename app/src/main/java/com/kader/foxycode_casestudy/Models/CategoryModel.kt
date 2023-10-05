package com.kader.foxycode_casestudy.Models

data class CategoryModel(
     val en: String? = null,
     val tr: String? = null,
     val words: List<WordModel> = listOf()
) {

     private var currentLanguage = "English" // Varsayılan dil İngilizce

     fun setTitleTurkish() {
          currentLanguage = "Turkish"
          // Türkçe başlık ayarla
          // Örneğin, tr değişkenini döndürebilirsin
     }

     fun setTitleEnglish() {
          currentLanguage = "English"
          // İngilizce başlık ayarla
          // Örneğin, en değişkenini döndürebilirsin
     }

     fun getCurrentTitle(): String {
          return if (currentLanguage == "Turkish") {
               tr.orEmpty()
          } else {
               en.orEmpty()
          }
     }
}
