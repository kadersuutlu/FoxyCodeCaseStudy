package com.kader.foxycode_casestudy.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kader.foxycode_casestudy.Models.WordModel
import com.kader.foxycode_casestudy.R

class WordFragment : Fragment() {

    private lateinit var textViewWord: TextView
    private lateinit var buttonPrevious: ImageView
    private lateinit var buttonNext: ImageView

    private lateinit var databaseReference: DatabaseReference

    private var words: MutableList<WordModel> = mutableListOf()

    private var currentWordIndex: Int = 0

    private var currentLanguage: String = "English"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_word, container, false)

        textViewWord = view.findViewById(R.id.textViewWord)
        buttonPrevious = view.findViewById(R.id.buttonPrevious)
        buttonNext = view.findViewById(R.id.buttonNext)

        val categoryId: String? = arguments?.getString("categoryId")
        if (categoryId != null) {
            Log.d("WordFragment", "CategoryId: $categoryId")

            databaseReference =
                FirebaseDatabase.getInstance("https://foxycodecasestudy-default-rtdb.europe-west1.firebasedatabase.app")
                    .getReference("categories")
                    .child(categoryId)
                    .child("words")
            Log.d("WordFragment", "Database reference: $databaseReference")


            buttonPrevious.setOnClickListener {
                Log.d("WordFragment", "Previous button clicked")
                showPreviousWord()
            }

            buttonNext.setOnClickListener {
                Log.d("WordFragment", "Next button clicked")
                showNextWord()
            }


            fetchWords()
        } else {

            Log.e("WordFragment", "CategoryId is null.")
        }

        return view
    }

    private fun fetchWords() {

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d("WordFragment", "OnDataChange.")
                if (dataSnapshot.exists()) {
                    for (wordSnapshot in dataSnapshot.children) {
                        Log.d("WordFragment", "Word: ${wordSnapshot.value}")
                    }

                    if (words.isNotEmpty()) {

                        words.clear()

                        for (wordSnapshot in dataSnapshot.children) {
                            val id = wordSnapshot.key
                            val enWord = wordSnapshot.child("en").getValue(String::class.java)
                            val trWord = wordSnapshot.child("tr").getValue(String::class.java)

                            if (id != null && enWord != null && trWord != null) {
                                val word = WordModel(id, enWord, trWord)
                                words.add(word)
                            }
                        }

                        Log.d("WordFragment", "Words list size: ${words.size}")


                        if (words.isNotEmpty()) {
                            showWord(currentWordIndex)
                        }
                    }
                } else {
                    Log.e("WordFragment", "DataSnapshot is empty.")
                }
            }


            override fun onCancelled(databaseError: DatabaseError) {
                val errorMessage = databaseError.message
                Log.e("WordFragment", "FirebaseError: $errorMessage")
            }
        })
    }


    private fun showWord(index: Int) {
        Log.d("WordFragment", "showWord called with index: $index")

        if (index in 0 until words.size) {
            val word = words[index]


            Log.d("WordFragment", "Word Index: $index")
            Log.d("WordFragment", "Word ID: ${word.id}")
            Log.d("WordFragment", "Word English: ${word.en}")
            Log.d("WordFragment", "Word Turkish: ${word.tr}")


            if (currentLanguage == "English") {
                textViewWord.text = word.en
            } else if (currentLanguage == "Turkish") {

                textViewWord.text = word.tr
            }

            Log.d("WordFragment", "Showing word: ${textViewWord.text}")
        } else {
            Log.e("WordFragment", "Invalid word index: $index")
        }
    }






    private fun showPreviousWord() {
        if (currentWordIndex > 0) {
            currentWordIndex--
            showWord(currentWordIndex)
        }
    }

    private fun showNextWord() {
        if (currentWordIndex < words.size - 1) {
            currentWordIndex++
            showWord(currentWordIndex)
        }
    }
}

