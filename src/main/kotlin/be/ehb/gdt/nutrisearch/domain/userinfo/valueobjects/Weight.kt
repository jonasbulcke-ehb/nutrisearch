package be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects

import java.time.LocalDateTime

class Weight(val measuredAt: LocalDateTime, val value: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Weight

        if (measuredAt != other.measuredAt) return false
        return value == other.value
    }

    override fun hashCode(): Int {
        var result = measuredAt.hashCode()
        result = 31 * result + value
        return result
    }
}
