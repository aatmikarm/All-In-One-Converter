package com.aatmik.allinoneconverter.fragment.converters

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.aatmik.allinoneconverter.databinding.FragmentJpgToPngBinding
import java.io.File
import java.io.FileOutputStream

class JpgToPngFragment : Fragment() {

    private var _binding: FragmentJpgToPngBinding? = null
    private val binding get() = _binding!!

    private var selectedImageUri: Uri? = null

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                selectedImageUri = uri
                binding.selectedImage.setImageURI(uri)
                binding.selectedImage.visibility = View.VISIBLE
                binding.convertButton.isEnabled = true
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJpgToPngBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.selectImageButton.setOnClickListener {
            pickImage()
        }

        binding.convertButton.setOnClickListener {
            convertJpgToPng()
        }
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        pickImageLauncher.launch(intent)
    }

    private fun convertJpgToPng() {
        selectedImageUri?.let { uri ->
            try {
                binding.progressBar.visibility = View.VISIBLE
                binding.convertButton.isEnabled = false

                val inputStream = requireContext().contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()

                val outputFile = File(
                    requireContext().getExternalFilesDir(null),
                    "converted_${System.currentTimeMillis()}.png"
                )

                FileOutputStream(outputFile).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                }

                binding.progressBar.visibility = View.GONE
                binding.convertButton.isEnabled = true

                Toast.makeText(
                    requireContext(),
                    "Converted successfully!\nSaved to: ${outputFile.absolutePath}",
                    Toast.LENGTH_LONG
                ).show()

                shareFile(Uri.fromFile(outputFile))

            } catch (e: Exception) {
                binding.progressBar.visibility = View.GONE
                binding.convertButton.isEnabled = true
                Toast.makeText(
                    requireContext(),
                    "Conversion failed: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun shareFile(fileUri: Uri) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, fileUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(shareIntent, "Share PNG file"))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}