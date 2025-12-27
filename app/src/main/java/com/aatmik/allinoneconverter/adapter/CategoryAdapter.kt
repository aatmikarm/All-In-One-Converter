package com.aatmik.allinoneconverter.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aatmik.allinoneconverter.R
import com.aatmik.allinoneconverter.model.Category

class CategoryAdapter(
    private val categories: ArrayList<Category>,
    private val onCategoryClick: (Int) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.category_item,
            parent, false
        )
        return CategoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.categoryName.text = category.name
        holder.categoryIcon.setImageResource(category.icon)

        val isSelected = position == selectedPosition
        val context = holder.itemView.context

        if (isSelected) {
            holder.categoryCard.setCardBackgroundColor(
                ContextCompat.getColor(context, R.color.colorPrimary)
            )
            holder.categoryName.setTextColor(
                ContextCompat.getColor(context, android.R.color.white)
            )
            holder.categoryIcon.setColorFilter(
                ContextCompat.getColor(context, android.R.color.white)
            )
        } else {
            holder.categoryCard.setCardBackgroundColor(
                ContextCompat.getColor(context, R.color.colorSurface)
            )
            holder.categoryName.setTextColor(
                ContextCompat.getColor(context, android.R.color.secondary_text_light)
            )
            holder.categoryIcon.clearColorFilter()
        }
    }

    override fun getItemCount(): Int = categories.size

    fun updateSelection(newPosition: Int) {
        val oldPosition = selectedPosition
        selectedPosition = newPosition
        notifyItemChanged(oldPosition)
        notifyItemChanged(newPosition)
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryCard: CardView = itemView.findViewById(R.id.categoryCard)
        val categoryName: TextView = itemView.findViewById(R.id.categoryName)
        val categoryIcon: ImageView = itemView.findViewById(R.id.categoryIcon)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    updateSelection(position)
                    onCategoryClick(position)
                }
            }
        }
    }
}