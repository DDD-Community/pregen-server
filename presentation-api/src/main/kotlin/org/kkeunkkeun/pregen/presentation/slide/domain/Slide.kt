package org.kkeunkkeun.pregen.presentation.slide.domain

import jakarta.persistence.*
import jakarta.persistence.FetchType.LAZY
import org.kkeunkkeun.pregen.presentation.file.domain.File
import org.kkeunkkeun.pregen.presentation.presentation.presentation.PresentationRequest

@Entity
class Slide(

    practiceId: Long,

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "image_file_id")
    val imageFile: File? = null,

    @Column(columnDefinition = "text")
    val script: String,

    @Column(columnDefinition = "text")
    val memo: String,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "slide_id")
    val id: Long? = null

    var practiceId: Long? = practiceId
        protected set

    val imageFilePath: String?
        get() = imageFile?.absolutePath

    companion object {
        fun from(practiceId: Long, imageFile: File?, request: PresentationRequest.SlideRequest): Slide {
            return Slide(practiceId, imageFile, request.script, request.memo)
        }
    }

    fun unmap() {
        this.practiceId = null
    }
}
