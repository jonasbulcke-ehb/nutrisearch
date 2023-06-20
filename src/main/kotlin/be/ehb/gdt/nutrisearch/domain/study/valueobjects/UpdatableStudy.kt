package be.ehb.gdt.nutrisearch.domain.study.valueobjects

import java.time.LocalDate

class UpdatableStudy(
    val subject: String,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val id: String,
)