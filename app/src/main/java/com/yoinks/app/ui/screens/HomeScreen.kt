package com.yoinks.app.ui.screens

import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.yoinks.app.data.*
import com.yoinks.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    initialSharedUrl: String? = null,
    onStartDownload: (MediaMetadata, QualityOption) -> Unit = { _, _ -> }
) {
    val context = LocalContext.current
    var urlInput by remember { mutableStateOf(initialSharedUrl ?: "") }
    var phase by remember { mutableStateOf<AppPhase>(AppPhase.Input) }
    var selectedOptionId by remember { mutableStateOf("mp3_320") }

    // Procesa el enlace si llega compartido desde otra app
    LaunchedEffect(initialSharedUrl) {
        if (!initialSharedUrl.isNullOrBlank()) {
            urlInput = initialSharedUrl
            phase = AppPhase.Probing("Analizando enlace compartido…")
            simulateProbe(initialSharedUrl) { probed ->
                phase = AppPhase.Ready(probed, DEFAULT_OPTIONS)
            }
        }
    }

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "yoinks",
                            fontWeight = FontWeight.Black,
                            fontSize = 24.sp,
                            color = BrandEmerald
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "mobile",
                            fontSize = 12.sp,
                            color = TextMuted,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .background(DarkSurfaceElevated, RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Pega un enlace y descárgalo con carátula y metadatos en 320 kbps.",
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            }

            // Barra de entrada y botón pegar
            item {
                OutlinedTextField(
                    value = urlInput,
                    onValueChange = { urlInput = it },
                    placeholder = { Text("https://youtube.com/watch?v=…", color = TextMuted) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = DarkSurface,
                        unfocusedContainerColor = DarkSurface,
                        focusedBorderColor = BrandIndigo,
                        unfocusedBorderColor = DarkBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    trailingIcon = {
                        IconButton(onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clipText = clipboard.primaryClip?.getItemAt(0)?.text?.toString()
                            if (!clipText.isNullOrBlank()) {
                                urlInput = clipText
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.ContentPaste,
                                contentDescription = "Pegar",
                                tint = TextSecondary
                            )
                        }
                    },
                    singleLine = true
                )
            }

            // Botón principal de Yoink / Inspeccionar
            item {
                Button(
                    onClick = {
                        if (urlInput.isNotBlank()) {
                            phase = AppPhase.Probing("Inspeccionando información…")
                            simulateProbe(urlInput) { probed ->
                                phase = AppPhase.Ready(probed, DEFAULT_OPTIONS)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandIndigo)
                ) {
                    Icon(imageVector = Icons.Default.Download, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Yoink It",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            // Vista según la fase actual
            when (val current = phase) {
                is AppPhase.Probing -> {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = DarkSurface),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(20.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(color = BrandEmerald, modifier = Modifier.size(28.dp))
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(text = current.message, color = TextPrimary)
                            }
                        }
                    }
                }

                is AppPhase.Ready -> {
                    item {
                        // Tarjeta de Vista Previa del Video / Audio
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = DarkSurface),
                            shape = RoundedCornerShape(18.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                if (!current.metadata.thumbnailUrl.isNullOrEmpty()) {
                                    AsyncImage(
                                        model = current.metadata.thumbnailUrl,
                                        contentDescription = "Carátula",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(180.dp)
                                            .clip(RoundedCornerShape(12.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                }

                                Text(
                                    text = current.metadata.title,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${current.metadata.artist} • ${MetadataCleaner.detectPlatformName(current.metadata.url)}",
                                    color = TextSecondary,
                                    fontSize = 13.sp
                                )

                                if (current.metadata.isPlaylist) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Colección detectada • ${current.metadata.playlistCount} canciones",
                                        color = BrandEmerald,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Selecciona la Calidad:",
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    items(current.options) { option ->
                        val isSelected = option.id == selectedOptionId
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedOptionId = option.id }
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) BrandEmerald else DarkBorder,
                                    shape = RoundedCornerShape(14.dp)
                                ),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) DarkSurfaceElevated else DarkSurface
                            ),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { selectedOptionId = option.id },
                                    colors = RadioButtonDefaults.colors(selectedColor = BrandEmerald)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = option.label,
                                        color = TextPrimary,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Button(
                            onClick = {
                                val opt = current.options.firstOrNull { it.id == selectedOptionId } ?: current.options[0]
                                phase = AppPhase.Downloading(
                                    progress = 0.1f,
                                    speed = "4.8 MB/s",
                                    currentItem = 1,
                                    totalItems = current.metadata.playlistCount,
                                    currentTitle = current.metadata.title
                                )
                                onStartDownload(current.metadata, opt)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BrandEmerald)
                        ) {
                            Text("Confirmar y Descargar", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                }

                is AppPhase.Downloading -> {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = DarkSurface),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Text(
                                    text = "Descargando ${current.currentTitle}",
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                LinearProgressIndicator(
                                    progress = current.progress,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(4.dp)),
                                    color = BrandEmerald,
                                    trackColor = DarkSurfaceElevated
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Item ${current.currentItem} de ${current.totalItems}",
                                        color = TextSecondary,
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        text = current.speed,
                                        color = BrandEmerald,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                is AppPhase.Done -> {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = DarkSurface),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = BrandEmerald,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "¡Descarga completada!",
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Guardado en tu carpeta de Música.",
                                    color = TextSecondary,
                                    fontSize = 13.sp
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                OutlinedButton(
                                    onClick = {
                                        phase = AppPhase.Input
                                        urlInput = ""
                                    },
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Descargar otro enlace", color = TextPrimary)
                                }
                            }
                        }
                    }
                }

                else -> {}
            }
        }
    }
}

// Helper simulador de inspección de metadatos
fun simulateProbe(url: String, onDone: (MediaMetadata) -> Unit) {
    val (artist, track) = MetadataCleaner.parseArtistAndTrack("Sample Track", "Sample Artist")
    val isPlaylist = MetadataCleaner.isPlaylistUrl(url)
    onDone(
        MediaMetadata(
            url = url,
            title = track,
            artist = artist,
            album = "Descargas Yoinks",
            thumbnailUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=600",
            isPlaylist = isPlaylist,
            playlistCount = if (isPlaylist) 12 else 1
        )
    )
}
