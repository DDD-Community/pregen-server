package org.kkeunkkeun.pregen.presentation.file.domain

import jakarta.persistence.*
import jakarta.persistence.EnumType.STRING

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
) {
    fun absolutePath(): String {
        return String.format("%s/%s", path, generatedName)
    }
}