package com.example.nfctools

data class NfcTagDetails(
    val serialNumber: String,
    val memorySize: Int,
    val usedMemory: Int?,
    val availableMemory: Int?,
    val technologies: List<String>,
    val tagType: String,
    val ndefMessage: String?,
    val atqa: String? = null,
    val sak: String? = null,
    val blockCount: Int? = null,
    val contentType: String?,
    val isWritable: Boolean?,
    val isPasswordProtected: Boolean?,
)