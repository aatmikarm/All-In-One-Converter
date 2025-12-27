package com.aatmik.allinoneconverter.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aatmik.allinoneconverter.R
import com.aatmik.allinoneconverter.activity.ConverterActivity
import com.aatmik.allinoneconverter.adapter.CategoryAdapter
import com.aatmik.allinoneconverter.adapter.ConverterAdapter
import com.aatmik.allinoneconverter.databinding.FragmentAllConvertersBinding
import com.aatmik.allinoneconverter.model.Converter
import com.aatmik.allinoneconverter.util.ConverterCategoriesUtil
import com.aatmik.allinoneconverter.util.ConverterUtils

class AllConvertersFragment : Fragment() {

    private var _binding: FragmentAllConvertersBinding? = null
    private val binding get() = _binding!!

    // RecyclerView for converters
    private lateinit var converterRV: RecyclerView
    private lateinit var converterAdapter: ConverterAdapter
    private lateinit var converterList: ArrayList<Converter>
    private lateinit var originalConverterList: ArrayList<Converter>

    // RecyclerView for categories
    private lateinit var categoriesRV: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter
    private var currentSelectedCategory = "All"
    private var currentCategoryIndex = 0

    // Collapsible category section
    private var isCategoryExpanded = false

    companion object {
        private const val CONVERTER_GRID_COLUMN_COUNT = 4
        private const val CATEGORY_GRID_COLUMN_COUNT = 3
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllConvertersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadConverters()
        setupCategoriesRecyclerView()
        setupCategoryCollapsible()
        setupConvertersRecyclerView()
        filterByCategory("All")
        setupSearch()
        setupClickListeners()
    }

    private fun loadConverters() {
        converterList = ArrayList(ConverterUtils.converterList)
        originalConverterList = ArrayList(ConverterUtils.converterList)
    }

    private fun setupCategoriesRecyclerView() {
        categoriesRV = binding.categoriesRV
        categoriesRV.layoutManager = GridLayoutManager(requireContext(), CATEGORY_GRID_COLUMN_COUNT)

        categoryAdapter = CategoryAdapter(ConverterCategoriesUtil.categories) { position ->
            val selectedCategory = ConverterCategoriesUtil.categories[position].name
            currentSelectedCategory = selectedCategory
            currentCategoryIndex = position
            filterByCategory(selectedCategory)
        }

        categoriesRV.adapter = categoryAdapter
    }

    private fun setupCategoryCollapsible() {
        binding.categoryHeader.setOnClickListener {
            toggleCategoryExpansion()
        }
    }

    private fun toggleCategoryExpansion() {
        if (isCategoryExpanded) {
            collapseView(binding.categoryExpandableContent)
            rotateIcon(binding.categoryExpandIcon, 180f, 0f)
            isCategoryExpanded = false
        } else {
            expandView(binding.categoryExpandableContent)
            rotateIcon(binding.categoryExpandIcon, 0f, 180f)
            isCategoryExpanded = true
        }
    }

    private fun expandView(view: View) {
        view.visibility = View.VISIBLE
        view.alpha = 0f
        view.animate()
            .alpha(1f)
            .setDuration(300)
            .start()
    }

    private fun collapseView(view: View) {
        view.animate()
            .alpha(0f)
            .setDuration(300)
            .withEndAction {
                view.visibility = View.GONE
            }
            .start()
    }

    private fun rotateIcon(view: View, fromDegree: Float, toDegree: Float) {
        view.animate()
            .rotation(toDegree)
            .setDuration(300)
            .start()
    }

    private fun setupConvertersRecyclerView() {
        converterRV = binding.converterRV
        converterRV.layoutManager = GridLayoutManager(requireContext(), CONVERTER_GRID_COLUMN_COUNT)

        converterAdapter = ConverterAdapter(converterList) { converter ->
            handleConverterSelection(converter.name)
        }

        converterRV.adapter = converterAdapter
    }

    private fun filterByCategory(categoryName: String) {
        val searchQuery = binding.searchEt.text.toString()

        val filteredList = if (searchQuery.isEmpty()) {
            ConverterCategoriesUtil.getConvertersForCategory(categoryName, originalConverterList)
        } else {
            ConverterCategoriesUtil.filterConvertersBySearch(searchQuery, categoryName, originalConverterList)
        }

        converterAdapter.updateConverterList(filteredList)

        if (isCategoryExpanded) {
            toggleCategoryExpansion()
        }
    }

    private fun setupSearch() {
        binding.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchQuery = s.toString()
                filterBySearch(searchQuery)

                binding.clearTextIv.visibility = if (!s.isNullOrEmpty()) View.VISIBLE else View.GONE
                if (!s.isNullOrEmpty()) {
                    binding.searchEt.isCursorVisible = true
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.clearTextIv.setOnClickListener {
            binding.searchEt.text.clear()
            binding.clearTextIv.visibility = View.GONE
            binding.searchEt.isCursorVisible = false
        }
    }

    private fun filterBySearch(searchQuery: String) {
        val filteredList = ConverterCategoriesUtil.filterConvertersBySearch(
            searchQuery,
            currentSelectedCategory,
            originalConverterList
        )
        converterAdapter.updateConverterList(filteredList)
    }

    private fun setupClickListeners() {
        binding.menuIv.setOnClickListener {
            showMenu()
        }
    }

    private fun showMenu() {
        Toast.makeText(requireContext(), "Menu clicked", Toast.LENGTH_SHORT).show()
    }

    private fun handleConverterSelection(converterName: String) {
        val intent = Intent(requireContext(), ConverterActivity::class.java).apply {
            putExtra("converterName", converterName)
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}