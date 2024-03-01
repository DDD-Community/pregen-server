package org.kkeunkkeun.pregen.presentation.practice.presentation

import org.kkeunkkeun.pregen.presentation.memorizationsentence.domain.MemorizationSentence
import org.kkeunkkeun.pregen.presentation.practice.domain.Practice
import org.kkeunkkeun.pregen.presentation.presentation.domain.Presentation
import org.kkeunkkeun.pregen.presentation.presentation.presentation.PresentationTime
import org.kkeunkkeun.pregen.presentation.slide.domain.Slide
import org.kkeunkkeun.pregen.presentation.slide.domain.SlideAggregate
import java.time.LocalDateTime

data class PracticeResponse(
    val presentationId: Long,
    val title: String,
    val timeLimit: PresentationTime,
    val alertTime: PresentationTime?,
    val practiceMode: PracticeMode,
    val activateNextSlideModal: Boolean,
    val slides: List<SlideDetail>,
    val createdAt: LocalDateTime,
    val modifiedAt: LocalDateTime?,
) {

    companion object {
        fun from(
            presentation: Presentation,
            practice: Practice,
            slides: List<SlideAggregate>,
            activateNextSlideModal: Boolean,
        ): PracticeResponse {
            return PracticeResponse(
                presentationId = presentation.id!!,
                title = practice.title,
                timeLimit = PresentationTime.from(presentation.timeLimit),
                alertTime = presentation.alertTime?.let { PresentationTime.from(it) },
                practiceMode = practice.mode,
                activateNextSlideModal = activateNextSlideModal,
                slides = slides.map { slide -> SlideDetail.from(slide.slide, slide.memorizationSentences) },
                createdAt = practice.createdAt,
                modifiedAt = practice.modifiedAt,
            )
        }
    }

    data class SlideDetail(
        val id: Long,
        val imageFileId: Long?,
        val imageFilePath: String?,
        val script: String,
        val memo: String,
        val memorizationSentences: List<MemorizationSentenceDetail>,
    ) {

        companion object {
            fun from(slide: Slide, sentences: List<MemorizationSentence>): SlideDetail {
                val memorizationSentences = sentences.map {
                    sentence -> MemorizationSentenceDetail.from(sentence, slide.script) }

                return SlideDetail(
                    id = slide.id!!,
                    imageFileId = slide.imageFile?.id,
                    imageFilePath = slide.imageFilePath,
                    script = slide.script,
                    memo = slide.memo,
                    memorizationSentences = memorizationSentences,
                )
            }
        }

    }

    data class MemorizationSentenceDetail(
        val offset: Int,
        val length: Int,
        val end: Int,
        val text: String,
    ) {

        companion object {
            fun from(sentence: MemorizationSentence, script: String): MemorizationSentenceDetail {
                val slicedText = script.slice(sentence.startIndex until sentence.endIndex)

                return MemorizationSentenceDetail(
                    offset = sentence.startIndex,
                    length = sentence.endIndex - sentence.startIndex,
                    end = sentence.endIndex,
                    text = slicedText,
                )
            }
        }
    }
}
