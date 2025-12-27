package com.aatmik.allinoneconverter.util

import com.aatmik.allinoneconverter.R
import com.aatmik.allinoneconverter.model.Category
import com.aatmik.allinoneconverter.model.Converter

object ConverterCategoriesUtil {

    val categories = arrayListOf(
        Category("All", R.drawable.all),
        Category("Main", R.drawable.category),
        Category("Tools", R.drawable.tools)
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