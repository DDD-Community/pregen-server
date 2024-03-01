package org.kkeunkkeun.pregen.presentation.practice.infrastructure

import com.linecorp.kotlinjdsl.dsl.jpql.jpql
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderContext
import com.linecorp.kotlinjdsl.support.spring.data.jpa.extension.createQuery
import jakarta.persistence.EntityManager
import org.kkeunkkeun.pregen.presentation.practice.domain.Practice
import org.kkeunkkeun.pregen.presentation.presentation.domain.Presentation
import org.springframework.stereotype.Repository

@Repository
class PracticeQueryRepository(
    private val entityManager: EntityManager,
) {

    val context = JpqlRenderContext()

    fun findLatestByAccountId(accountId: Long): Practice? {
        val query = jpql {
            select(
                entity(Practice::class)
            ).from(
                entity(Practice::class),
                join(Presentation::class).on(
                    path(Practice::presentationId).equal(path(Presentation::id))
                )
            ).where(
                and(
                    path(Presentation::accountId).equal(accountId),
                    path(Practice::startedAt).isNotNull(),
                )
            ).orderBy(
                path(Practice::startedAt).desc()
            )
        }

        return entityManager.createQuery(query, context)
            .resultList
            .firstOrNull()
    }
}