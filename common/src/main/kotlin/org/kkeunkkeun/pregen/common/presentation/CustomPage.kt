package org.kkeunkkeun.pregen.common.presentation

import org.springframework.data.domain.Page

/**
 * Spring의 Pagination이 기본적으로 제공하는 속성 중, 필요한 속성만 제공하기 위한 커스텀 클래스.
 *
 * @see org.springframework.data.domain.Page
 */
data class CustomPage<T>(
    val content: List<T>,
    val totalPage: Int,
    val totalElements: Long,
    val empty: Boolean,
) {
    companion object {
        fun <T : Any?> from(page: Page<T>): CustomPage<T> {
            return CustomPage(page.content, page.totalPages, page.totalElements, page.isEmpty)
        }
    }
}
