package be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects

import java.time.LocalDate

class Study(val id: String, val subject: String, val startDate: LocalDate, val endDate: LocalDate?) {
    val isActive: Boolean
        get() = endDate?.isAfter(LocalDate.now()) ?: true
}
