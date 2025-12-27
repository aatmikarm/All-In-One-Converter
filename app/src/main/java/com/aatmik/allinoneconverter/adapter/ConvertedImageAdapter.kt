package com.aatmik.allinoneconverter.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.aatmik.allinoneconverter.R
import com.aatmik.allinoneconverter.model.ConvertedImage

class ConvertedImageAdapter(
    private val convertedImages: List<ConvertedImage>
) : RecyclerView.Adapter<ConvertedImageAdapter.ConvertedImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConvertedImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_converted_image,
            parent,
            false
        )
        return ConvertedImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConvertedImageViewHolder, position: Int) {
        val convertedImage = convertedImages[position]

        holder.imageView.setImageURI(convertedImage.convertedUri)
        holder.formatText.text = convertedImage.format

        holder.shareButton.setOnClickListener {
            shareImage(holder.itemView, convertedImage)
        }

        holder.cardView.setOnClickListener {
            viewImage(holder.itemView, convertedImage)
        }
    }

    override fun getItemCount(): Int = convertedImages.size

    private fun shareImage(view: View, convertedImage: ConvertedImage) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_STREAM, convertedImage.convertedUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        view.context.startActivity(Intent.createChooser(shareIntent, "Share Image"))
    }

    private fun viewImage(view: View, convertedImage: ConvertedImage) {
        val viewIntent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(convertedImage.convertedUri, "image/*")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        view.context.startActivity(viewIntent)
    }

    class ConvertedImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.cardView)
        val imageView: ImageView = itemView.findViewById(R.id.convertedImageView)
        val formatText: TextView = itemView.findViewById(R.id.formatText)
        val shareButton: ImageView = itemView.findViewById(R.id.shareButton)
    }
}