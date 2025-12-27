package com.aatmik.allinoneconverter.model

import android.net.Uri

data class ConvertedImage(
    val originalUri: Uri,
    val convertedUri: Uri,
    val outputPath: String,
    val format: String
)