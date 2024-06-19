package com.example.nfctools

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NfcViewModel : ViewModel() {
    private val _nfcData = MutableStateFlow<NfcTagDetails?>(null)
    val nfcData: StateFlow<NfcTagDetails?> = _nfcData

    private val _showWriteDialog = MutableStateFlow(false)
    val showWriteDialog: StateFlow<Boolean> = _showWriteDialog

    private val _isLocation = MutableStateFlow(false)
    val isLocation: StateFlow<Boolean> = _isLocation

    fun updateNfcData(nfcTagDetails: NfcTagDetails) {
        _nfcData.value = nfcTagDetails
    }

    fun setShowWriteDialog(visible: Boolean) {
        _showWriteDialog.value = visible
    }

    fun setIsLocation(value: Boolean) {
        _isLocation.value = value
    }
}