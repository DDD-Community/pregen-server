package org.kkeunkkeun.pregen.presentation.slide.infrastructure

import com.linecorp.kotlinjdsl.dsl.jpql.jpql
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderContext
import com.linecorp.kotlinjdsl.support.spring.data.jpa.extension.createQuery
import jakarta.persistence.EntityManager
import org.kkeunkkeun.pregen.presentation.memorizationsentence.domain.MemorizationSentence
import org.kkeunkkeun.pregen.presentation.slide.domain.Slide
import org.kkeunkkeun.pregen.presentation.slide.domain.SlideAggregate
import org.springframework.stereotype.Repository

@Repository
class SlideQueryRepository(
    private val entityManager: EntityManager,
) {

    val context = JpqlRenderContext()

    fun findAggregateBySlideId(slideId: Long): SlideAggregate {
        val slideQuery = jpql {
            select(
                entity(Slide::class),
            ).from(
                entity(Slide::class),
            ).where(path(Slide::id).eq(slideId))
        }

        val sentenceQuery = jpql {
            select(
                entity(MemorizationSentence::class),
            ).from(
                entity(Slide::class),
                join(MemorizationSentence::class).on(
                    path(Slide::id).equal(path(MemorizationSentence::slideId))
                )
            ).where(path(Slide::id).eq(slideId))
        }

        return SlideAggregate(
            slide = entityManager.createQuery(slideQuery, context).singleResult,
            memorizationSentences = entityManager.createQuery(sentenceQuery, context).resultList,
        )
    }
}