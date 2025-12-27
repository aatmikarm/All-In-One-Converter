package com.aatmik.allinoneconverter.util

import com.aatmik.allinoneconverter.R
import com.aatmik.allinoneconverter.model.Converter

object ConverterUtils {

    val converterList = arrayListOf(
        // Main Converters
        Converter("Images", R.drawable.image_converter, "Main"),
        Converter("PDF", R.drawable.document_converter, "Main"),
        Converter("Audio", R.drawable.audio_converter, "Main"),
        Converter("Video", R.drawable.video_converter, "Main"),

        // Tools
        Converter("Image Compressor", R.drawable.compress, "Tools"),
        Converter("PDF Compressor", R.drawable.compress, "Tools"),
        Converter("PDF Merger", R.drawable.merge, "Tools"),
        Converter("PDF Splitter", R.drawable.split, "Tools"),
        Converter("OCR Scanner", R.drawable.ocr, "Tools"),
        Converter("QR Code Generator", R.drawable.qr, "Tools"),
        Converter("Barcode Generator", R.drawable.barcode, "Tools")
    )

    fun getAllCategories(): List<String> {
        return converterList.map { it.category }.distinct().sorted()
    }

    fun getConvertersByCategory(category: String): ArrayList<Converter> {
        return if (category == "All") {
            ArrayList(converterList)
        } else {
            ArrayList(converterList.filter { it.category == category })
        }
    }

    fun searchConverters(query: String): ArrayList<Converter> {
        return if (query.isBlank()) {
            ArrayList(converterList)
        } else {
            ArrayList(converterList.filter {
                it.name.contains(query, ignoreCase = true)
            })
        }
    }

    fun searchConvertersWithCategory(query: String, category: String): ArrayList<Converter> {
        val filteredByCategory = getConvertersByCategory(category)
        return if (query.isBlank()) {
            filteredByCategory
        } else {
            ArrayList(filteredByCategory.filter {
                it.name.contains(query, ignoreCase = true)
            })
        }
    }
}