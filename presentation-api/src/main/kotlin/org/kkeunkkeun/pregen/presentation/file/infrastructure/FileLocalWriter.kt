package org.kkeunkkeun.pregen.presentation.file.infrastructure

import org.kkeunkkeun.pregen.presentation.file.domain.File
import org.kkeunkkeun.pregen.presentation.file.service.FileWriter
import org.springframework.stereotype.Repository
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@Repository
class FileLocalWriter: FileWriter {

    override fun writeMultipartFile(file: MultipartFile, path: String, physicalName: String): File {
        val targetLocation = Paths.get(path)

        // Resolve the file path to prevent overwriting issues and keep original file name
        val targetPath = targetLocation.resolve(physicalName)

        // Copy the file to the target location (replacing existing file with the same name)
        Files.copy(file.inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING)

        return File.from(file, path, physicalName)
    }
}