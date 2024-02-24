package org.kkeunkkeun.pregen.presentation.file.service

import org.kkeunkkeun.pregen.presentation.file.domain.File

interface FileRepository {

    fun save(file: File)

    fun findById(fileId: Long?): File?
}