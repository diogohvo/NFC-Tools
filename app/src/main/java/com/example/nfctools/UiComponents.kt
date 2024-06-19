@file:Suppress("SpellCheckingInspection")

package com.example.nfctools

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.FileCopy
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.LayersClear
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Nfc
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Preview
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.TextSnippet
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.nfctools.ui.theme.main

//READ composables
@Composable
fun BeforeReadComposable(viewModel: NfcViewModel = viewModel(), navController: NavController) {
    val nfcTagDetails by viewModel.nfcData.collectAsState()
    if (nfcTagDetails != null) {
        navController.navigate("Details")
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier.size(50.dp),
                imageVector = Icons.Filled.Nfc,
                contentDescription = "NFC Icon",
            )
            Text("Encoste uma tag NFC", fontSize = 18.sp)
        }
    }
}


@Composable
fun AfterReadComposable(viewModel: NfcViewModel = viewModel()) {
    val nfcTagDetails by viewModel.nfcData.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.TopCenter,
    ) {
        nfcTagDetails?.let { details ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                //type
                Card(
                    colors = CardDefaults.cardColors(containerColor = main),
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.Memory, contentDescription = null, modifier = Modifier.padding(end = 10.dp))
                        Column {
                            Text("Tipo de tag", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Text(details.tagType, fontSize = 14.sp)
                        }
                    }
                }

                //serial number
                Card(
                    colors = CardDefaults.cardColors(containerColor = main),
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.ConfirmationNumber, contentDescription = null, modifier = Modifier.padding(end = 10.dp))
                        Column {
                            Text("Número de série", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Text(details.serialNumber, fontSize = 14.sp)
                        }
                    }
                }

                //technologies
                Card(
                    colors = CardDefaults.cardColors(containerColor = main),
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.Nfc, contentDescription = null, modifier = Modifier.padding(end = 10.dp))
                        Column {
                            Text("Tecnologias", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Text(details.technologies.joinToString(", "), fontSize = 14.sp)
                        }
                    }
                }

                //atqa
                details.atqa?.let {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = main),
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Filled.Preview, contentDescription = null, modifier = Modifier.padding(end = 10.dp))
                            Column {
                                Text("ATQA", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                Text("0x${it}", fontSize = 14.sp)
                            }
                        }
                    }
                }

                //sak
                details.sak?.let {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = main),
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Filled.Badge, contentDescription = null, modifier = Modifier.padding(end = 10.dp))
                            Column {
                                Text("SAK", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                Text("0x${it}", fontSize = 14.sp)
                            }
                        }
                    }
                }

                //memory size
                Card(
                    colors = CardDefaults.cardColors(containerColor = main),
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.Storage, contentDescription = null, modifier = Modifier.padding(end = 10.dp))
                        Column {
                            Text("Memória total", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Text("${details.memorySize} bytes", fontSize = 14.sp)
                        }
                    }
                }

                //memory used
                Card(
                    colors = CardDefaults.cardColors(containerColor = main),
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.Storage, contentDescription = null, modifier = Modifier.padding(end = 10.dp))
                        Column {
                            Text("Memória em uso", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Text("${details.usedMemory} bytes", fontSize = 14.sp)
                        }
                    }
                }

                //memory available
                Card(
                    colors = CardDefaults.cardColors(containerColor = main),
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.Storage, contentDescription = null, modifier = Modifier.padding(end = 10.dp))
                        Column {
                            Text("Memória disponível", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Text("${details.availableMemory} bytes", fontSize = 14.sp)
                        }
                    }
                }

                //block count
                details.blockCount?.let {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = main),
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Filled.GridOn, contentDescription = null, modifier = Modifier.padding(end = 10.dp))
                            Column {
                                Text("Blocos de memória", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                Text("$it", fontSize = 14.sp)
                            }
                        }
                    }
                }

                //isWritable
                details.isWritable?.let {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = main),
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Filled.EditNote, contentDescription = null, modifier = Modifier.padding(end = 10.dp))
                            Column {
                                Text("Gravável", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                Text("$it", fontSize = 14.sp)
                            }
                        }
                    }
                }

                //isPasswordProtected
                details.isPasswordProtected?.let {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = main),
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Filled.Key, contentDescription = null, modifier = Modifier.padding(end = 10.dp))
                            Column {
                                Text("Protegido por password", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                Text("$it", fontSize = 14.sp)
                            }
                        }
                    }
                }

                //ndefMessage
                details.ndefMessage?.let {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = main),
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Filled.Mail, contentDescription = null, modifier = Modifier.padding(end = 10.dp))
                            Column {
                                Text("Tipo de conteúdo: ${details.contentType}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                Text(it, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }
        } ?: run {
            Text("Nenhuns dados NFC...", fontSize = 18.sp)
        }
    }
}

//------------------------------

//WRITE components
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteComposable(
    viewModel: NfcViewModel = viewModel(),
    onMessageEntered: (String) -> Unit,
    onCancel: () -> Unit,
) {
    var showInputDialog by remember { mutableStateOf(false) }
    val showWriteDialog by viewModel.showWriteDialog.collectAsState()
    val isLocation by viewModel.isLocation.collectAsState()
    var selectedCardTitle by remember { mutableStateOf("") }

    if (showInputDialog) {
        InputDialog(
            title = selectedCardTitle,
            onConfirm = { message ->
                onMessageEntered(message)
                showInputDialog = false
                viewModel.setShowWriteDialog(true)
            },
            onDismiss = {
                showInputDialog = false
                viewModel.setIsLocation(false) 
                        },
            isLocation = isLocation
        )
    }

    if (showWriteDialog) {
        WriteDialog(
            onDismiss = {
                viewModel.setShowWriteDialog(false)
                viewModel.setIsLocation(false)
                onCancel()
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            //text
            Card(
                colors = CardDefaults.cardColors(containerColor = main),
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                onClick = {
                    selectedCardTitle = "texto"
                    showInputDialog = true
                }
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.TextSnippet, contentDescription = null, modifier = Modifier.padding(end = 10.dp))
                    Column {
                        Text("Texto", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text("Adicionar Texto", fontSize = 14.sp)
                    }
                }
            }
            //URL
            Card(
                colors = CardDefaults.cardColors(containerColor = main),
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                onClick = {
                    selectedCardTitle = "URL"
                    showInputDialog = true
                }
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.Link, contentDescription = null, modifier = Modifier.padding(end = 10.dp))
                    Column {
                        Text("URL", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text("Adicionar um URL", fontSize = 14.sp)
                    }
                }
            }
            //Location
            Card(
                colors = CardDefaults.cardColors(containerColor = main),
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                onClick = {
                    selectedCardTitle = "Localização atual"
                    showInputDialog = true
                    viewModel.setIsLocation(true)
                }
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.LocationOn, contentDescription = null, modifier = Modifier.padding(end = 10.dp))
                    Column {
                        Text("Localização atual", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text("Adicionar localização atual", fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun WriteDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Encoste uma tag NFC") },
        text = {
            Icon(
                modifier = Modifier.size(50.dp),
                imageVector = Icons.Filled.Nfc,
                contentDescription = "NFC Icon",
            )
        },
        confirmButton = {},
        dismissButton = {
            Button(
                onClick = { onDismiss() }
            ) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun InputDialog(
    title: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
    isLocation: Boolean,
) {
    var message by remember { mutableStateOf("") }
    var locationRetrieved by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(isLocation) {
        if (isLocation) {
            getCurrentLocation(context) { latitude, longitude ->
                message = "geo:$latitude,$longitude"
                locationRetrieved = true
            }
        }
    }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Escrever um $title") },
        text = {
            if(isLocation){
                if (locationRetrieved) {
                    Text(text = "Localização obtida: $message")
                } else {
                    Text(text = "Obtendo localização atual...")
                }
            }else {
                OutlinedTextField(
                    value = message,
                    placeholder = { Text(text = "Insere o $title a escrever na tag:") },
                    onValueChange = { message = it },
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    message = if (title == "URL" && !message.startsWith("http://") && !message.startsWith("https://")) {
                        "http://$message"
                    } else { message }
                    onConfirm(message)
                    onDismiss()
                }
            ) {
                Text("Escrever")
            }
        },
        dismissButton = {
            Button(
                onClick = { onDismiss() }
            ) {
                Text("Cancelar")
            }
        }
    )
}

//-----------------------------------

//OTHER
@Composable
fun OtherComposable(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            //copy
            Card(
                colors = CardDefaults.cardColors(containerColor = main),
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.FileCopy, contentDescription = null, modifier = Modifier.padding(end = 10.dp))
                    Column {
                        Text("Copiar Tag", fontSize = 20.sp)
                    }
                }
            }

            //delete
            Card(
                colors = CardDefaults.cardColors(containerColor = main),
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.Delete, contentDescription = null, modifier = Modifier.padding(end = 10.dp))
                    Column {
                        Text("Apagar Tag", fontSize = 20.sp)
                    }
                }
            }

            //format memory
            Card(
                colors = CardDefaults.cardColors(containerColor = main),
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.LayersClear, contentDescription = null, modifier = Modifier.padding(end = 10.dp))
                    Column {
                        Text("Formatar memória", fontSize = 20.sp)
                    }
                }
            }

            //password protect
            Card(
                colors = CardDefaults.cardColors(containerColor = main),
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.Password, contentDescription = null, modifier = Modifier.padding(end = 10.dp))
                    Column {
                        Text("Proteger por password", fontSize = 20.sp)
                    }
                }
            }

            //block
            Card(
                colors = CardDefaults.cardColors(containerColor = main),
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.Lock, contentDescription = null, modifier = Modifier.padding(end = 10.dp))
                    Column {
                        Text("Bloquear a tag", fontSize = 20.sp)
                    }
                }
            }
        }
    }
}