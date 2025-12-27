package com.aatmik.allinoneconverter.util

import com.aatmik.allinoneconverter.R
import com.aatmik.allinoneconverter.model.Converter

object ConverterUtils {

    val converterList = arrayListOf(
        // Image Converters
        Converter("JPG to PNG", R.drawable.ic_image_converter, "Image"),
        Converter("PNG to JPG", R.drawable.ic_image_converter, "Image"),
        Converter("HEIC to JPG", R.drawable.ic_image_converter, "Image"),
        Converter("WEBP to PNG", R.drawable.ic_image_converter, "Image"),
        Converter("PNG to WEBP", R.drawable.ic_image_converter, "Image"),
        Converter("JPG to WEBP", R.drawable.ic_image_converter, "Image"),
        Converter("BMP to PNG", R.drawable.ic_image_converter, "Image"),
        Converter("GIF to PNG", R.drawable.ic_image_converter, "Image"),
        Converter("SVG to PNG", R.drawable.ic_image_converter, "Image"),
        Converter("ICO to PNG", R.drawable.ic_image_converter, "Image"),

        // Document Converters
        Converter("PDF to Word", R.drawable.ic_document_converter, "Document"),
        Converter("Word to PDF", R.drawable.ic_document_converter, "Document"),
        Converter("PDF to Excel", R.drawable.ic_document_converter, "Document"),
        Converter("Excel to PDF", R.drawable.ic_document_converter, "Document"),
        Converter("PDF to PowerPoint", R.drawable.ic_document_converter, "Document"),
        Converter("PowerPoint to PDF", R.drawable.ic_document_converter, "Document"),
        Converter("PDF to Text", R.drawable.ic_document_converter, "Document"),
        Converter("Text to PDF", R.drawable.ic_document_converter, "Document"),
        Converter("Word to Text", R.drawable.ic_document_converter, "Document"),
        Converter("HTML to PDF", R.drawable.ic_document_converter, "Document"),
        Converter("PDF to HTML", R.drawable.ic_document_converter, "Document"),
        Converter("Markdown to PDF", R.drawable.ic_document_converter, "Document"),

        // Audio Converters
        Converter("MP3 to WAV", R.drawable.ic_audio_converter, "Audio"),
        Converter("WAV to MP3", R.drawable.ic_audio_converter, "Audio"),
        Converter("M4A to MP3", R.drawable.ic_audio_converter, "Audio"),
        Converter("FLAC to MP3", R.drawable.ic_audio_converter, "Audio"),
        Converter("OGG to MP3", R.drawable.ic_audio_converter, "Audio"),
        Converter("AAC to MP3", R.drawable.ic_audio_converter, "Audio"),
        Converter("MP3 to AAC", R.drawable.ic_audio_converter, "Audio"),
        Converter("WMA to MP3", R.drawable.ic_audio_converter, "Audio"),

        // Video Converters
        Converter("MP4 to AVI", R.drawable.ic_video_converter, "Video"),
        Converter("AVI to MP4", R.drawable.ic_video_converter, "Video"),
        Converter("MOV to MP4", R.drawable.ic_video_converter, "Video"),
        Converter("MKV to MP4", R.drawable.ic_video_converter, "Video"),
        Converter("WEBM to MP4", R.drawable.ic_video_converter, "Video"),
        Converter("FLV to MP4", R.drawable.ic_video_converter, "Video"),
        Converter("WMV to MP4", R.drawable.ic_video_converter, "Video"),
        Converter("3GP to MP4", R.drawable.ic_video_converter, "Video"),
        Converter("Video to GIF", R.drawable.ic_video_converter, "Video"),
        Converter("Video to Audio", R.drawable.ic_video_converter, "Video"),

        // Archive Converters
        Converter("ZIP to RAR", R.drawable.ic_archive_converter, "Archive"),
        Converter("RAR to ZIP", R.drawable.ic_archive_converter, "Archive"),
        Converter("7Z to ZIP", R.drawable.ic_archive_converter, "Archive"),
        Converter("TAR to ZIP", R.drawable.ic_archive_converter, "Archive"),
        Converter("Extract ZIP", R.drawable.ic_archive_converter, "Archive"),
        Converter("Extract RAR", R.drawable.ic_archive_converter, "Archive"),
        Converter("Create ZIP", R.drawable.ic_archive_converter, "Archive"),

        // eBook Converters
        Converter("EPUB to PDF", R.drawable.ic_ebook_converter, "eBook"),
        Converter("MOBI to PDF", R.drawable.ic_ebook_converter, "eBook"),
        Converter("PDF to EPUB", R.drawable.ic_ebook_converter, "eBook"),
        Converter("AZW to PDF", R.drawable.ic_ebook_converter, "eBook"),
        Converter("EPUB to MOBI", R.drawable.ic_ebook_converter, "eBook"),

        // Data Format Converters
        Converter("JSON to CSV", R.drawable.ic_data_converter, "Data"),
        Converter("CSV to JSON", R.drawable.ic_data_converter, "Data"),
        Converter("XML to JSON", R.drawable.ic_data_converter, "Data"),
        Converter("JSON to XML", R.drawable.ic_data_converter, "Data"),
        Converter("CSV to Excel", R.drawable.ic_data_converter, "Data"),
        Converter("Excel to CSV", R.drawable.ic_data_converter, "Data"),
        Converter("YAML to JSON", R.drawable.ic_data_converter, "Data"),

        // Image Tools
        Converter("Image Compressor", R.drawable.ic_compress, "Tools"),
        Converter("Image Resizer", R.drawable.ic_resize, "Tools"),
        Converter("PDF Compressor", R.drawable.ic_compress, "Tools"),
        Converter("PDF Merger", R.drawable.ic_merge, "Tools"),
        Converter("PDF Splitter", R.drawable.ic_split, "Tools"),
        Converter("OCR Scanner", R.drawable.ic_ocr, "Tools"),
        Converter("QR Code Generator", R.drawable.ic_qr, "Tools"),
        Converter("Barcode Generator", R.drawable.ic_barcode, "Tools")
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