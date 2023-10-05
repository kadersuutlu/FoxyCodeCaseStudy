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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_category, container, false)

        // categoryAdapter'ı başlat
        categoryAdapter = CategoryAdapter()

        // RecyclerView'in özelliklerini ayarla
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewCategories)
        val spanCount = 2
        val layoutManager = GridLayoutManager(context, spanCount)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = categoryAdapter
        Log.d("CategoryFragment", "onCreateView is called")
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

            // Dil seçildiğinde yapılacak işlemler
            // Örneğin, RecyclerView'yi güncelle
            updateRecyclerView(selectedLanguage)
        }

        Log.d("CategoryFragment", "onViewCreated is called")

        databaseReference = FirebaseDatabase.getInstance("https://foxycodecasestudy-default-rtdb.europe-west1.firebasedatabase.app").getReference("categories")

        Log.d("CategoryFragment", "Database reference: $databaseReference")

        // Debug log
        Log.d("CategoryFragment", "onViewCreated is called")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val categories = mutableListOf<CategoryModel>()

                // Debug log
                Log.d("CategoryFragment", "onDataChange is called")

                for (categorySnapshot in dataSnapshot.children) {
                    val enName = categorySnapshot.child("en").getValue(String::class.java)
                    val trName = categorySnapshot.child("tr").getValue(String::class.java)

                    //Log.d("CategoryFragment", "enName: $enName, trName: $trName")

                    if (enName != null && trName != null) {
                        val category = CategoryModel(enName, trName)
                        categories.add(category)
                    }
                }

                categoryAdapter.setCategories(categories)

                Log.d("CategoryFragment", "RecyclerView updated. Item count: ${categoryAdapter.itemCount}")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                val errorMessage = databaseError.message
                Log.e("CategoryFragment", "FirebaseError: $errorMessage")
            }
        })
    }
    private fun updateRecyclerView(selectedLanguage: String) {

        Log.d("CategoryFragment", "Selected Language: $selectedLanguage")
        categoryAdapter.setSelectedLanguage(selectedLanguage)

        val categories = categoryAdapter.getCategories()

        // Dil seçimine göre verileri güncelle
        for (category in categories) {
            category.apply {
                if (selectedLanguage == "TÜRKÇE") {
                    setTitleTurkish()
                } else if(selectedLanguage=="ENGLISH") {
                    setTitleEnglish()
                }
            }
        }

        // RecyclerView'yi güncelle
        categoryAdapter.notifyDataSetChanged()

        Log.d("CategoryFragment", "RecyclerView updated. Item count: ${categoryAdapter.itemCount}")
    }


}
