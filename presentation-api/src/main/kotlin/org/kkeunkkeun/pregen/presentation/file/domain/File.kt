package org.kkeunkkeun.pregen.presentation.file.domain

import jakarta.persistence.*
import jakarta.persistence.EnumType.STRING
import org.kkeunkkeun.pregen.common.domain.BaseEntity
import org.kkeunkkeun.pregen.presentation.file.domain.FileType.IMAGE
import org.springframework.web.multipart.MultipartFile

@Entity
class File(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    val id: Long? = null,

    @Enumerated(STRING)
    @Column(length = 30)
    val fileType: FileType,

    @Column(length = 2000)
    val path: String,

    val originalName: String,

    val generatedName: String
): BaseEntity() {

    val absolutePath: String
        get() = "$path/$generatedName"

    companion object {

        fun from(file: MultipartFile, path: String, generatedName: String): File {
            val originalName = file.originalFilename ?: throw IllegalStateException("original name not provided.")

            return File(
                null,
                fileType = IMAGE,
                path = path.replace(".", ""),
                originalName = originalName,
                generatedName = generatedName,
            )
        }
    }
}