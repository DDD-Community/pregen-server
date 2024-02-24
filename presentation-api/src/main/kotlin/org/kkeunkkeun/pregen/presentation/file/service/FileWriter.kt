package org.kkeunkkeun.pregen.presentation.file.service

import org.kkeunkkeun.pregen.presentation.file.domain.File
import org.springframework.web.multipart.MultipartFile

interface FileWriter {

    fun writeMultipartFile(file: MultipartFile, path: String, physicalName: String): File

}