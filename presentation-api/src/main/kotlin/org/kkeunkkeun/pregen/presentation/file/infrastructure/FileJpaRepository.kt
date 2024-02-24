package org.kkeunkkeun.pregen.presentation.file.infrastructure

import org.kkeunkkeun.pregen.presentation.file.domain.File
import org.springframework.data.jpa.repository.JpaRepository

interface FileJpaRepository: JpaRepository<File, Long> {
}