package com.kader.foxycode_casestudy.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kader.foxycode_casestudy.Adapter.CategoryAdapter
import com.kader.foxycode_casestudy.Models.CategoryModel
import com.kader.foxycode_casestudy.R

class CategoryFragment : Fragment() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var categoryAdapter: CategoryAdapter

    private var selectedCategoryId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_category, container, false)


        categoryAdapter = CategoryAdapter { clickedCategory ->
            selectedCategoryId = clickedCategory.getCategoryId()
            val wordFragment = WordFragment()


            val bundle = Bundle()
            bundle.putString("categoryId", selectedCategoryId)
            wordFragment.arguments = bundle


            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentcontainer, wordFragment)
                .addToBackStack(null)
                .commit()
        }


        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewCategories)
        val spanCount = 2
        val layoutManager = GridLayoutManager(context, spanCount)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = categoryAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val radioGroup: RadioGroup = view.findViewById(R.id.radioGroupLanguage)

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val selectedLanguage = when (checkedId) {
                R.id.radioButtonTr -> "TÜRKÇE"
                R.id.radiButtonEn -> "ENGLISH"
                else -> "Default Language" // Varsayılan dil
            }


            updateRecyclerView(selectedLanguage)
        }

        databaseReference =
            FirebaseDatabase.getInstance("https://foxycodecasestudy-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("categories")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val categories = mutableListOf<CategoryModel>()

                for (categorySnapshot in dataSnapshot.children) {
                    val enName = categorySnapshot.child("en").getValue(String::class.java)
                    val trName = categorySnapshot.child("tr").getValue(String::class.java)

                    if (enName != null && trName != null) {
                        val categoryId = categorySnapshot.key
                        val category = CategoryModel(enName, trName, emptyList(),categoryId)
                        categories.add(category)
                    }
                }

                categoryAdapter.setCategories(categories)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                val errorMessage = databaseError.message
                Log.e("CategoryFragment", "FirebaseError: $errorMessage")
            }
        })
    }

    private fun updateRecyclerView(selectedLanguage: String) {
        categoryAdapter.setSelectedLanguage(selectedLanguage)

        val categories = categoryAdapter.getCategories()


        for (category in categories) {
            category.apply {
                if (selectedLanguage == "TÜRKÇE") {
                    setTitleTurkish()
                } else {
                    setTitleEnglish()
                }
            }
        }


        categoryAdapter.notifyDataSetChanged()
    }
}

