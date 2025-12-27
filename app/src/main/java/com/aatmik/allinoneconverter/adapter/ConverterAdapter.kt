package com.aatmik.allinoneconverter.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aatmik.allinoneconverter.R
import com.aatmik.allinoneconverter.model.Converter
import com.bumptech.glide.Glide

class ConverterAdapter(
    private var converterList: ArrayList<Converter>,
    private val onItemClick: (Converter) -> Unit
) : RecyclerView.Adapter<ConverterAdapter.ConverterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConverterViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.converter_item,
            parent, false
        )
        return ConverterViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ConverterViewHolder, position: Int) {
        val converter = converterList[position]
        holder.itemName.text = converter.name

        Glide.with(holder.itemView.context)
            .load(converter.image)
            .dontAnimate()
            .dontTransform()
            .override(200, 200)
            .centerInside()
            .into(holder.itemImage)
    }

    override fun getItemCount(): Int = converterList.size

    fun updateConverterList(updatedList: ArrayList<Converter>) {
        val previousSize = converterList.size
        converterList.clear()
        converterList.addAll(updatedList)

        if (previousSize == updatedList.size) {
            notifyItemRangeChanged(0, updatedList.size)
        } else {
            notifyDataSetChanged()
        }
    }

    fun isEmpty(): Boolean = converterList.isEmpty()

    inner class ConverterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName: TextView = itemView.findViewById(R.id.itemName)
        val itemImage: ImageView = itemView.findViewById(R.id.itemImage)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION && position < converterList.size) {
                    val converter = converterList[position]
                    onItemClick(converter)
                }
            }
        }
    }
}