package org.kkeunkkeun.pregen.presentation.file.presentation

import org.kkeunkkeun.pregen.presentation.file.domain.File

data class FileResponse(
    val id: Long,
    val path: String,
) {

    companion object {
        fun from(file: File): FileResponse {
            return FileResponse(file.id!!, file.absolutePath)
        }
    }
}
