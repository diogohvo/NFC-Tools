@file:Suppress("SpellCheckingInspection")

package com.example.nfctools

import android.annotation.SuppressLint
import android.content.Context
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.Tag
import android.nfc.tech.MifareClassic
import android.nfc.tech.Ndef
import android.nfc.tech.NfcA
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

internal fun writeNfcTag(context: Context, tag: Tag, message: String, isLocation: Boolean) {
    val ndef = Ndef.get(tag)
    if (ndef != null) {
        try {
            ndef.connect()
            if (ndef.isWritable) {
                val ndefMessage: NdefMessage
                if(isLocation){
                    // Create a Geolocation Record
                    val locationRecord = NdefRecord.createExternal("com.example.nfctools", "location", message.toByteArray())
                    ndefMessage = NdefMessage(arrayOf(locationRecord))
                }else if (message.startsWith("http://") || message.startsWith("https://")) {
                    // Create a URI NdefRecord
                    val uriRecord = NdefRecord.createUri(message)
                    ndefMessage = NdefMessage(arrayOf(uriRecord))
                } else {
                    // Create a Text NdefRecord
                    val textRecord = NdefRecord.createTextRecord("en", message)
                    ndefMessage = NdefMessage(arrayOf(textRecord))
                }

                ndef.writeNdefMessage(ndefMessage)
                Toast.makeText(context, "Tag escrita com sucesso", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "A Tag não é gravável", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Error writing NDEF message", e)
            Toast.makeText(context, "Falha a escrever a tag", Toast.LENGTH_LONG).show()
        } finally {
            try {
                ndef.close()
            } catch (e: Exception) {
                Log.e("MainActivity", "Error closing NDEF connection", e)
            }
        }
    }
}

internal fun readNfcTag(tag: Tag, nfcViewModel: NfcViewModel) {
    val serialNumber = tag.id.joinToString(":") { "%02x".format(it) }
    val technologies = tag.techList.map { it.removePrefix("android.nfc.tech.") }
    val tagType = getTagType(tag)
    var contentType: String? = null

    var ndefMessage: String? = null
    var memorySize = 0
    var isWritable: Boolean? = null
    var usedMemory: Int? = null
    var availableMemory: Int? = null

    val ndef = Ndef.get(tag)
    if (ndef != null) {
        try {
            ndef.connect()
            memorySize = ndef.maxSize
            isWritable = ndef.isWritable
            ndef.ndefMessage?.let {
                ndefMessage = it.records.joinToString("\n") { record ->
                    val payload = record.payload
                    contentType = when {
                        record.tnf == NdefRecord.TNF_WELL_KNOWN && record.type.contentEquals(NdefRecord.RTD_TEXT) -> "Texto"
                        record.tnf == NdefRecord.TNF_WELL_KNOWN && record.type.contentEquals(NdefRecord.RTD_URI) -> "URI"
                        record.tnf == NdefRecord.TNF_MIME_MEDIA -> "MIME Media"
                        record.tnf == NdefRecord.TNF_EXTERNAL_TYPE && String(record.type) == "com.example.nfctools:location" -> "Localização"
                        else -> "Unknown"
                    }

                    val text = when (contentType) {
                        "Texto" -> String(payload.copyOfRange(3, payload.size)) // Skip the first 3 bytes for text
                        "URI" -> String(payload) // Do not skip bytes for URI
                        "Localização" -> String(payload) // Do not skip bytes for Location
                        else -> String(payload) // Default case, do not skip bytes
                    }

                    text
                }
                usedMemory = it.toByteArray().size
                availableMemory = memorySize - usedMemory!!
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Error reading NDEF message", e)
        } finally {
            try {
                ndef.close()
            } catch (e: Exception) {
                Log.e("MainActivity", "Error closing NDEF connection", e)
            }
        }
    }

    val nfcA = NfcA.get(tag)
    val atqa = nfcA?.atqa?.joinToString("") { "%02x".format(it) }
    val sak = nfcA?.sak?.toString()
    val isProtected = isNfcTagPasswordProtected(nfcA)
    val mifareClassic = MifareClassic.get(tag)
    val blockCount = mifareClassic?.blockCount

    val nfcTagDetails = NfcTagDetails(
        serialNumber = serialNumber,
        memorySize = memorySize,
        usedMemory = usedMemory,
        availableMemory = availableMemory,
        technologies = technologies,
        tagType = tagType,
        ndefMessage = ndefMessage,
        atqa = atqa,
        sak = sak,
        blockCount = blockCount,
        contentType = contentType,
        isWritable = isWritable,
        isPasswordProtected = isProtected,
    )

    MainScope().launch {
        nfcViewModel.updateNfcData(nfcTagDetails)
    }
}

//function to retrieve tag type, requires knowledge
private fun getTagType(tag: Tag): String {
    val nfcA = NfcA.get(tag)
    val sak = nfcA?.sak ?: return "Unknown"
    val atqa = nfcA.atqa

    return if (atqa != null && atqa.isNotEmpty() && atqa[0] == 0x44.toByte()) {
        when (sak) {
            0x00.toShort() -> "NXP - NTAG213"
            else -> "NXP - Unknown Tag"
        }
    } else {
        "Unknown"
    }
}

// Function to check if the NTAG213 tag and similar is password protected
private fun isNfcTagPasswordProtected(nfcA: NfcA?): Boolean {
    if (nfcA == null) return false

    return try {
        nfcA.connect()
        // Read the page 42 (NTAG213)
        val response = nfcA.transceive(byteArrayOf(0x30.toByte(), 0x29.toByte()))
        if (response.size < 4) {
            // Invalid response
            false
        } else {
            //non-FF value indicates password protection is enabled
            val auth0 = response[3].toInt() and 0xFF
            auth0 != 0xFF
        }
    } catch (e: Exception) {
        Log.e("MainActivity", "Error reading NFCA connection", e)
        false
    } finally {
        try {
            nfcA.close()
        } catch (e: Exception) {
            Log.e("MainActivity", "Error closing NFCA connection", e)
        }
    }
}

// Function to get current location
@SuppressLint("MissingPermission")
internal fun getCurrentLocation(context: Context, callback: (latitude: Double, longitude: Double) -> Unit) {
    val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    fusedLocationClient.lastLocation
        .addOnSuccessListener { location ->
            location?.let {
                callback(it.latitude, it.longitude)
            } ?: run {
                callback(0.0,0.0) //null location
            }
        }
}