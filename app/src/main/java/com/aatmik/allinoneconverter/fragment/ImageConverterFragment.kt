package com.aatmik.allinoneconverter.fragment.converters

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.aatmik.allinoneconverter.R
import com.aatmik.allinoneconverter.adapter.ConvertedImageAdapter
import com.aatmik.allinoneconverter.databinding.FragmentImageConverterBinding
import com.aatmik.allinoneconverter.model.ConvertedImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class ImageConverterFragment : Fragment() {

    private var _binding: FragmentImageConverterBinding? = null
    private val binding get() = _binding!!

    private var selectedImageUris = mutableListOf<Uri>()
    private var convertedImages = mutableListOf<ConvertedImage>()
    private lateinit var convertedImageAdapter: ConvertedImageAdapter

    private val imageFormats = listOf("JPG", "PNG", "WEBP", "BMP")
    private var inputFormat = "JPG"
    private var outputFormat = "PNG"

    private var conversionJob: Job? = null

    private val pickMultipleImagesLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            selectedImageUris.clear()
            result.data?.let { intent ->
                intent.clipData?.let { clipData ->
                    for (i in 0 until clipData.itemCount) {
                        selectedImageUris.add(clipData.getItemAt(i).uri)
                    }
                } ?: intent.data?.let { uri ->
                    selectedImageUris.add(uri)
                }
            }

            if (selectedImageUris.isNotEmpty()) {
                binding.selectedImagesCount.text = "${selectedImageUris.size} image(s) selected"
                binding.selectedImagesCount.visibility = View.VISIBLE
                binding.convertButton.isEnabled = true
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageConverterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpinners()
        setupRecyclerView()
        setupClickListeners()
    }

    private fun setupSpinners() {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            imageFormats
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        binding.inputFormatSpinner.adapter = adapter
        binding.outputFormatSpinner.adapter = adapter

        binding.inputFormatSpinner.setSelection(0)
        binding.outputFormatSpinner.setSelection(1)

        binding.inputFormatSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                inputFormat = imageFormats[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.outputFormatSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                outputFormat = imageFormats[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupRecyclerView() {
        convertedImageAdapter = ConvertedImageAdapter(convertedImages)
        binding.convertedImagesRV.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = convertedImageAdapter

            // Set RecyclerView height to 70% of screen height
            post {
                val displayMetrics = resources.displayMetrics
                val screenHeight = displayMetrics.heightPixels
                val recyclerViewHeight = (screenHeight * 0.8).toInt()

                layoutParams = layoutParams.apply {
                    height = recyclerViewHeight
                }
            }
        }
    }

    private fun setupClickListeners() {
        binding.selectSingleImageButton.setOnClickListener {
            pickImages(false)
        }

        binding.selectMultipleImagesButton.setOnClickListener {
            pickImages(true)
        }

        binding.convertButton.setOnClickListener {
            if (inputFormat == outputFormat) {
                Toast.makeText(
                    requireContext(),
                    "Input and output formats are the same!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            convertImages()
        }

        binding.clearButton.setOnClickListener {
            clearAll()
        }

        binding.shareAllButton.setOnClickListener {
            shareAllImages()
        }
    }

    private fun pickImages(multiple: Boolean) {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
            if (multiple) {
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            }
        }
        pickMultipleImagesLauncher.launch(intent)
    }

    private fun convertImages() {
        if (selectedImageUris.isEmpty()) return

        binding.progressBar.visibility = View.VISIBLE
        binding.progressText.visibility = View.VISIBLE
        binding.convertButton.isEnabled = false
        binding.selectSingleImageButton.isEnabled = false
        binding.selectMultipleImagesButton.isEnabled = false

        convertedImages.clear()
        convertedImageAdapter.notifyDataSetChanged()

        val isSingleImage = selectedImageUris.size == 1

        conversionJob = CoroutineScope(Dispatchers.IO).launch {
            val total = selectedImageUris.size

            selectedImageUris.forEachIndexed { index, uri ->
                try {
                    val progress = ((index + 1) * 100) / total

                    withContext(Dispatchers.Main) {
                        binding.progressText.text = "Converting ${index + 1}/$total ($progress%)"
                        binding.progressBar.progress = progress
                    }

                    val inputStream = requireContext().contentResolver.openInputStream(uri)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    inputStream?.close()

                    val format = when (outputFormat) {
                        "PNG" -> Bitmap.CompressFormat.PNG
                        "WEBP" -> Bitmap.CompressFormat.WEBP
                        else -> Bitmap.CompressFormat.JPEG
                    }

                    val quality = if (outputFormat == "PNG") 100 else 90

                    val originalFileName = getFileNameFromUri(uri)

                    val newFileName = if (isSingleImage) {
                        "AIO_Converter_${originalFileName}.${outputFormat.lowercase()}"
                    } else {
                        "AIO_Converter_${originalFileName}_${index + 1}.${outputFormat.lowercase()}"
                    }

                    val mimeType = when (outputFormat) {
                        "PNG" -> "image/png"
                        "WEBP" -> "image/webp"
                        "BMP" -> "image/bmp"
                        else -> "image/jpeg"
                    }

                    val savedUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        val values = ContentValues().apply {
                            put(MediaStore.Images.Media.DISPLAY_NAME, newFileName)
                            put(MediaStore.Images.Media.MIME_TYPE, mimeType)
                            put(MediaStore.Images.Media.RELATIVE_PATH, "${Environment.DIRECTORY_DCIM}/AIO Converter")
                        }

                        val collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
                        val imageUri = requireContext().contentResolver.insert(collection, values)

                        imageUri?.let { uri ->
                            requireContext().contentResolver.openOutputStream(uri)?.use { outputStream ->
                                bitmap.compress(format, quality, outputStream)
                            }
                        }
                        imageUri
                    } else {
                        val dcimDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                        val appFolder = File(dcimDir, "AIO Converter")
                        if (!appFolder.exists()) {
                            appFolder.mkdirs()
                        }

                        val outputFile = File(appFolder, newFileName)
                        FileOutputStream(outputFile).use { out ->
                            bitmap.compress(format, quality, out)
                        }

                        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                        mediaScanIntent.data = Uri.fromFile(outputFile)
                        requireContext().sendBroadcast(mediaScanIntent)

                        Uri.fromFile(outputFile)
                    }

                    savedUri?.let { resultUri ->
                        val convertedImage = ConvertedImage(
                            originalUri = uri,
                            convertedUri = resultUri,
                            outputPath = newFileName,
                            format = outputFormat
                        )

                        withContext(Dispatchers.Main) {
                            convertedImages.add(convertedImage)
                            convertedImageAdapter.notifyItemInserted(convertedImages.size - 1)
                        }
                    }

                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireContext(),
                            "Failed to convert image ${index + 1}: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            withContext(Dispatchers.Main) {
                binding.progressBar.visibility = View.GONE
                binding.progressText.visibility = View.GONE
                binding.convertButton.isEnabled = true
                binding.selectSingleImageButton.isEnabled = true
                binding.selectMultipleImagesButton.isEnabled = true

                if (convertedImages.isNotEmpty()) {
                    binding.convertedImagesSection.visibility = View.VISIBLE

                    // Auto-scroll to converted images section
                    binding.root.post {
                        binding.root.smoothScrollTo(0, binding.convertedImagesSection.top)
                    }

                    Toast.makeText(
                        requireContext(),
                        "Successfully converted ${convertedImages.size} image(s)!\nSaved to Gallery",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun getFileNameFromUri(uri: Uri): String {
        var fileName = "image"
        requireContext().contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
            if (nameIndex != -1 && cursor.moveToFirst()) {
                fileName = cursor.getString(nameIndex)
                fileName = fileName.substringBeforeLast(".")
                fileName = fileName.replace(Regex("[^a-zA-Z0-9_-]"), "_")
            }
        }
        return fileName
    }

    private fun shareAllImages() {
        if (convertedImages.isEmpty()) {
            Toast.makeText(requireContext(), "No images to share", Toast.LENGTH_SHORT).show()
            return
        }

        val uris = ArrayList<Uri>()
        convertedImages.forEach { uris.add(it.convertedUri) }

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND_MULTIPLE
            putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
            type = "image/*"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        startActivity(Intent.createChooser(shareIntent, "Share All Images"))
    }

    private fun clearAll() {
        selectedImageUris.clear()
        convertedImages.clear()
        convertedImageAdapter.notifyDataSetChanged()

        binding.selectedImagesCount.visibility = View.GONE
        binding.convertedImagesSection.visibility = View.GONE
        binding.convertButton.isEnabled = false
        binding.progressBar.progress = 0
    }

    override fun onDestroyView() {
        super.onDestroyView()
        conversionJob?.cancel()
        _binding = null
    }
}