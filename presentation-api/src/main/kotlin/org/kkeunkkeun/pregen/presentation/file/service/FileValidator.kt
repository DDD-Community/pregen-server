package org.kkeunkkeun.pregen.presentation.file.service

import org.kkeunkkeun.pregen.common.domain.Constant.Companion.ALLOWED_AUDIO_FILE_EXTENSIONS
import org.kkeunkkeun.pregen.common.domain.Constant.Companion.ALLOWED_FILE_EXTENSIONS
import org.kkeunkkeun.pregen.common.domain.Constant.Companion.ALLOWED_IMAGE_FILE_EXTENSIONS
import org.kkeunkkeun.pregen.common.domain.Constant.Companion.MAX_IMAGE_FILE_SIZE
import org.kkeunkkeun.pregen.presentation.file.infrastructure.FileProperties
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class FileValidator(
    private val fileProperties: FileProperties,
) {

    fun validate(file: MultipartFile) {
        if (file.isEmpty) {
            throw IllegalStateException("The file is empty. Please select a non-empty file.")
        }

        // Check the file size (10MB = 10 * 1024 * 1024 bytes)
        val maxFileSize = MAX_IMAGE_FILE_SIZE
        if (file.size > maxFileSize) {
            throw IllegalStateException("The file exceeds the maximum allowed size of 10MB.")
        }

        // Check the file type
        if (!ALLOWED_FILE_EXTENSIONS.contains(file.contentType)) {
            throw IllegalStateException("Invalid file type.")
        }
    }

    fun detectPath(file: MultipartFile): String {
        if (ALLOWED_IMAGE_FILE_EXTENSIONS.contains(file.contentType)) {
            return fileProperties.fullThumbnailPath
        }

        if (ALLOWED_AUDIO_FILE_EXTENSIONS.contains(file.contentType)) {
            return fileProperties.fullAudioPath
        }

        throw IllegalStateException("Invalid file type.")
    }
}