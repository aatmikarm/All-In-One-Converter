package com.aatmik.allinoneconverter.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.aatmik.allinoneconverter.R
import com.aatmik.allinoneconverter.databinding.ActivityConverterBinding
import com.aatmik.allinoneconverter.fragment.converters.ImageConverterFragment
import com.aatmik.allinoneconverter.fragment.converters.PlaceholderFragment

class ConverterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConverterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConverterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdgeInsets()

        val converterName = intent.getStringExtra("converterName")

        supportActionBar?.title = converterName
        binding.converterTitle.text = converterName ?: "Converter"

        loadConverterFragment(converterName)
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

    private fun loadConverterFragment(converterName: String?) {
        val fragment: Fragment = when (converterName) {
            "Images" -> ImageConverterFragment()
            "PDF" -> PlaceholderFragment.newInstance(converterName)
            "Audio" -> PlaceholderFragment.newInstance(converterName)
            "Video" -> PlaceholderFragment.newInstance(converterName)
            "Image Compressor" -> PlaceholderFragment.newInstance(converterName)
            "PDF Compressor" -> PlaceholderFragment.newInstance(converterName)
            "PDF Merger" -> PlaceholderFragment.newInstance(converterName)
            "PDF Splitter" -> PlaceholderFragment.newInstance(converterName)
            "OCR Scanner" -> PlaceholderFragment.newInstance(converterName)
            "QR Code Generator" -> PlaceholderFragment.newInstance(converterName)
            "Barcode Generator" -> PlaceholderFragment.newInstance(converterName)
            else -> PlaceholderFragment.newInstance(converterName ?: "Unknown Converter")
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.converterFragmentContainer, fragment)
            .commit()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}