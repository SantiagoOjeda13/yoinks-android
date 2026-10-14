package com.yoinks.app.data

enum class MediaKind {
    AUDIO,
    VIDEO
}

data class QualityOption(
    val id: String,
    val label: String,
    val kind: MediaKind,
    val bitrate: String? = null,
    val resolution: String? = null
)

val DEFAULT_OPTIONS = listOf(
    QualityOption("mp3_320", "MP3 · 320 kbps (Calidad Máxima)", MediaKind.AUDIO, bitrate = "320k"),
    QualityOption("mp3_256", "MP3 · 256 kbps (Alta Fidelidad)", MediaKind.AUDIO, bitrate = "256k"),
    QualityOption("mp3_192", "MP3 · 192 kbps (Estándar Ligero)", MediaKind.AUDIO, bitrate = "192k"),
    QualityOption("video_1080", "Video MP4 · 1080p Full HD", MediaKind.VIDEO, resolution = "1080p"),
    QualityOption("video_720", "Video MP4 · 720p HD", MediaKind.VIDEO, resolution = "720p")
)

data class MediaMetadata(
    val url: String,
    val title: String,
    val artist: String,
    val album: String,
    val year: String? = null,
    val durationSeconds: Long? = null,
    val thumbnailUrl: String? = null,
    val isPlaylist: Boolean = false,
    val playlistCount: Int = 1
)

sealed class AppPhase {
    object Input : AppPhase()
    data class Probing(val message: String = "Inspeccionando enlace…") : AppPhase()
    data class Ready(val metadata: MediaMetadata, val options: List<QualityOption>) : AppPhase()
    data class Downloading(
        val progress: Float,
        val speed: String,
        val currentItem: Int = 1,
        val totalItems: Int = 1,
        val currentTitle: String = ""
    ) : AppPhase()
    data class Done(val savedPath: String) : AppPhase()
    data class Error(val message: String) : AppPhase()
}
