package org.kkeunkkeun.pregen.presentation.file.infrastructure

import org.kkeunkkeun.pregen.presentation.file.domain.File
import org.kkeunkkeun.pregen.presentation.file.service.FileRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class FileRepositoryImpl(
    private val fileJpaRepository: FileJpaRepository
): FileRepository {

    override fun save(file: File) {
        fileJpaRepository.save(file)
    }

    override fun findById(fileId: Long?): File? = fileId?.let {
        fileJpaRepository.findByIdOrNull(it)
    }
}