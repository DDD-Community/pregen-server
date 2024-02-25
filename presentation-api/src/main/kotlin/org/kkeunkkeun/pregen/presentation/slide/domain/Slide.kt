package org.kkeunkkeun.pregen.presentation.slide.domain

import jakarta.persistence.*
import jakarta.persistence.FetchType.LAZY
import org.kkeunkkeun.pregen.common.domain.BaseEntity
import org.kkeunkkeun.pregen.presentation.file.domain.File
import org.kkeunkkeun.pregen.presentation.presentation.presentation.PresentationRequest

@Entity
class Slide(

    practiceId: Long,

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "image_file_id")
    val imageFile: File? = null,

    audioFile: File? = null,

    @Column(columnDefinition = "text")
    val script: String,

    memo: String,
): BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "slide_id")
    val id: Long? = null

    var practiceId: Long? = practiceId
        protected set

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "audio_file_id")
    var audioFile: File? = audioFile
        protected set

    @Column(columnDefinition = "text")
    var memo: String = memo
        protected set

    val imageFilePath: String?
        get() = imageFile?.absolutePath

    val audioFilePath: String?
        get() = audioFile?.absolutePath

    companion object {
        fun from(practiceId: Long, imageFile: File?, request: PresentationRequest.SlideRequest): Slide {
            return Slide(practiceId, imageFile, null, request.script, request.memo)
        }
    }

    fun unmap() {
        this.practiceId = null
    }

    fun done(memo: String, audioFile: File?) {
        this.memo = memo
        this.audioFile = audioFile
    }
}
