package com.aatmik.allinoneconverter.util

import com.aatmik.allinoneconverter.R
import com.aatmik.allinoneconverter.model.Category
import com.aatmik.allinoneconverter.model.Converter

object ConverterCategoriesUtil {

    val categories = arrayListOf(
        Category("All", R.drawable.ic_all),
        Category("Image", R.drawable.ic_image_converter),
        Category("Document", R.drawable.ic_document_converter),
        Category("Audio", R.drawable.ic_audio_converter),
        Category("Video", R.drawable.ic_video_converter),
        Category("Archive", R.drawable.ic_archive_converter),
        Category("eBook", R.drawable.ic_ebook_converter),
        Category("Data", R.drawable.ic_data_converter),
        Category("Tools", R.drawable.ic_tools)
    )

    fun getConvertersForCategory(categoryName: String, allConverters: ArrayList<Converter>): ArrayList<Converter> {
        return if (categoryName == "All") {
            ArrayList(allConverters)
        } else {
            ArrayList(allConverters.filter { it.category == categoryName })
        }
    }

    fun filterConvertersBySearch(query: String, categoryName: String, allConverters: ArrayList<Converter>): ArrayList<Converter> {
        val categoryFiltered = getConvertersForCategory(categoryName, allConverters)

        return if (query.isBlank()) {
            categoryFiltered
        } else {
            ArrayList(categoryFiltered.filter {
                it.name.contains(query, ignoreCase = true)
            })
        }
    }
}