package com.aatmik.allinoneconverter.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.aatmik.allinoneconverter.databinding.ActivityConverterBinding

class ConverterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConverterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConverterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdgeInsets()

        val converterName = intent.getStringExtra("converterName")

        binding.converterTitle.text = converterName ?: "Converter"

        // TODO: Load appropriate converter fragment based on converterName
        showConverterInfo(converterName)
    }

    private fun setupEdgeToEdgeInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                view.paddingLeft,
                insets.top,
                view.paddingRight,
                insets.bottom
            )
            windowInsets
        }
    }

    private fun showConverterInfo(converterName: String?) {
        Toast.makeText(this, "Converter: $converterName", Toast.LENGTH_SHORT).show()
        // TODO: Implement actual conversion logic
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}