package com.kader.foxycode_casestudy

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CategoryAdapter(private var categories: List<CategoryModel>) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    fun setCategories(categories: List<CategoryModel>) {
        this.categories = categories
        notifyDataSetChanged()
        Log.d("CategoryAdapter", "Categories updated. Count: ${categories.size}")
        Log.d("CategoryAdapter", "First category: ${categories.firstOrNull()?.en}")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cardview, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]
        holder.bind(category)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewTrName: TextView = itemView.findViewById(R.id.textViewCategoryTr)
        private val textViewEnName: TextView = itemView.findViewById(R.id.textViewCategoryEn)

        fun bind(category: CategoryModel) {
            textViewTrName.text = category.tr
            textViewEnName.text = category.en
        }
    }

}
