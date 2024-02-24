package org.kkeunkkeun.pregen.common.domain

class Constant {

    companion object {

        const val SESSION_ID_HEADER_NAME = "X-SESSION-ID"

        const val MAX_IMAGE_FILE_SIZE = 10 * 1024 * 1024

        val ALLOWED_IMAGE_FILE_EXTENSIONS = setOf("image/jpeg", "image/png")

        val ALLOWED_AUDIO_FILE_EXTENSIONS = setOf("audio/mpeg")

        val ALLOWED_FILE_EXTENSIONS = ALLOWED_AUDIO_FILE_EXTENSIONS + ALLOWED_IMAGE_FILE_EXTENSIONS
    }
}