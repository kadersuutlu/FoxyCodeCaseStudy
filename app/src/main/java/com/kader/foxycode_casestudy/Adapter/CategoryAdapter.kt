package com.kader.foxycode_casestudy.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kader.foxycode_casestudy.Models.CategoryModel
import com.kader.foxycode_casestudy.R

class CategoryAdapter(private val onItemClick: (CategoryModel) -> Unit) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private val categories = ArrayList<CategoryModel>()
    private var selectedLanguage: String = "English"

    fun setSelectedLanguage(language: String) {
        selectedLanguage = language
    }

    fun setCategories(categories: List<CategoryModel>) {
        this.categories.clear()
        this.categories.addAll(categories)
        notifyDataSetChanged()
        //Log.d("CategoryAdapter", "Categories updated. Count: ${categories.size}")
        //Log.d("CategoryAdapter", "First category: ${categories.firstOrNull()?.en}")
    }

    fun getCategories(): List<CategoryModel> {
        return categories
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cardview, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.bind(category, selectedLanguage)
        holder.itemView.setOnClickListener {
            onItemClick(category)
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewTrName: TextView = itemView.findViewById(R.id.textViewCategoryTr)
        private val textViewEnName: TextView = itemView.findViewById(R.id.textViewCategoryEn)

        fun bind(category: CategoryModel, selectedLanguage: String) {
            if (selectedLanguage == "TÜRKÇE") {
                textViewTrName.text = category.tr
                textViewEnName.text = category.en
            } else {
                textViewTrName.text = category.en
                textViewEnName.text = category.tr
            }
        }
    }
}
