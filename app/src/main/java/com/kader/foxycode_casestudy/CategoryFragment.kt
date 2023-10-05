package com.kader.foxycode_casestudy

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CategoryFragment : Fragment() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_category, container, false)

        // categoryAdapter'ı başlat
        categoryAdapter = CategoryAdapter(emptyList())

        // RecyclerView'in özelliklerini ayarla
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewCategories)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        recyclerView.setHasFixedSize(false)
        recyclerView.itemAnimator = null
        recyclerView.adapter = categoryAdapter
        Log.d("CategoryFragment", "onCreateView is called")
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
}
