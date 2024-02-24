package org.kkeunkkeun.pregen.presentation.file.service

import org.kkeunkkeun.pregen.presentation.file.domain.File
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
@Transactional
class FileUploadService(
    private val validator: FileValidator,
    private val fileRepository: FileRepository,
    private val fileWriter: FileWriter,
) {

    fun upload(file: MultipartFile): File {
        validator.validate(file)

        return saveAndWriteFile(file)
    }

    fun uploadBulk(files: List<MultipartFile>): List<File> {
        files.forEach { file -> validator.validate(file) }

        return files.map { file -> this.saveAndWriteFile(file) }
    }

    private fun saveAndWriteFile(file: MultipartFile): File {
        val fileEntity = fileWriter.writeMultipartFile(
            file = file,
            path = validator.detectPath(file),
            physicalName = generateFilePhysicalName(file)
        )

        fileRepository.save(fileEntity)

        return fileEntity
    }

    private fun generateFilePhysicalName(file: MultipartFile): String {
        val extension = StringUtils.getFilenameExtension(file.originalFilename)
        val generatedFileName = "${UUID.randomUUID()}.$extension"

        return generatedFileName
    }
}