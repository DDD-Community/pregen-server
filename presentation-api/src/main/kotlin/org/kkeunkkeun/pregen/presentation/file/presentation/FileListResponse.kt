package org.kkeunkkeun.pregen.presentation.file.presentation

import org.kkeunkkeun.pregen.presentation.file.domain.File

data class FileListResponse(
    val files: List<FileResponse>,
) {

    companion object {
        fun from(files: List<File>): FileListResponse {
            val fileResponses = files.map { file -> FileResponse.from(file) }

            return FileListResponse(fileResponses)
        }
    }
}
