package org.kkeunkkeun.pregen.presentation.file.infrastructure

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "file")
data class FileProperties(
    val basePath: String = "",
    val thumbnailPath: String = ""
) {
    val fullThumbnailPath: String
        get() = "$basePath/$thumbnailPath"
            .replace("//", "/")
}
