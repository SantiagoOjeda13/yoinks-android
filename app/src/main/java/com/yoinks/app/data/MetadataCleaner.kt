package com.yoinks.app.data

object MetadataCleaner {

    private val NOISE_REGEX = Regex(
        "(?i)\\s*([(\\[])\\s*(?:official\\s+(?:music\\s+)?video|video\\s+oficial|official\\s+audio|audio\\s+oficial|lyrics?|letra|remaster(?:ed)?|4k|hd|hq|clip|extended|version)\\s*([)\\]])"
    )

    fun cleanTitle(raw: String): String {
        return raw.replace(NOISE_REGEX, "").trim()
    }

    fun parseArtistAndTrack(title: String, uploader: String?): Pair<String, String> {
        val cleaned = cleanTitle(title)
        val separators = listOf(" - ", " – ", " — ", " : ", " | ")
        for (sep in separators) {
            if (cleaned.contains(sep)) {
                val parts = cleaned.split(sep, limit = 2)
                val artist = parts[0].trim()
                val track = parts[1].trim()
                if (artist.isNotEmpty() && track.isNotEmpty()) {
                    return Pair(cleanArtistName(artist), track)
                }
            }
        }
        val fallbackArtist = cleanArtistName(uploader ?: "Unknown Artist")
        return Pair(fallbackArtist, cleaned)
    }

    private fun cleanArtistName(artist: String): String {
        return artist
            .replace(Regex("(?i)\\s*(?:VEVO|- Topic|Official|Music|Records)\\b"), "")
            .trim()
            .ifEmpty { "Various Artists" }
    }

    fun isPlaylistUrl(url: String): Boolean {
        return url.contains("list=") || url.contains("/playlist") || url.contains("/sets/") || url.contains("/album/")
    }

    fun detectPlatformName(url: String): String {
        val lower = url.lowercase()
        return when {
            lower.contains("youtube.com") || lower.contains("youtu.be") -> "YouTube"
            lower.contains("instagram.com") -> "Instagram"
            lower.contains("tiktok.com") -> "TikTok"
            lower.contains("x.com") || lower.contains("twitter.com") -> "X (Twitter)"
            lower.contains("soundcloud.com") -> "SoundCloud"
            else -> "Web"
        }
    }
}
