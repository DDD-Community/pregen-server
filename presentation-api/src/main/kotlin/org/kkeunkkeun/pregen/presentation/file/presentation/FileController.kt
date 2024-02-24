package org.kkeunkkeun.pregen.presentation.file.presentation

import org.kkeunkkeun.pregen.presentation.file.service.FileUploadService
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/files")
class FileController(
    private val fileUploadService: FileUploadService,
) {

    @PostMapping("/upload")
    fun uploadFile(@RequestPart(name = "file") multipartFile: MultipartFile): ResponseEntity<FileResponse> {
        val fileEntity = fileUploadService.upload(multipartFile)

        return ResponseEntity.status(CREATED)
            .body(FileResponse.from(fileEntity))
    }
}