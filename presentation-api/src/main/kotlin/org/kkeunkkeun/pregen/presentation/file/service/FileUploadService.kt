package org.kkeunkkeun.pregen.presentation.file.service

import org.kkeunkkeun.pregen.presentation.file.domain.File
import org.kkeunkkeun.pregen.presentation.file.infrastructure.FileProperties
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@Service
class FileUploadService(
    private val validator: FileValidator,
    private val fileRepository: FileRepository,
    private val fileWriter: FileWriter,
    private val fileProperties: FileProperties,
) {

    fun upload(file: MultipartFile): File {
        validator.validate(file)

        val fileEntity = fileWriter.writeMultipartFile(
            file = file,
            path = fileProperties.fullThumbnailPath,
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