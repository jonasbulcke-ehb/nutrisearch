package be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects

import java.time.LocalDateTime

class WeightMeasurement(
    val value: Int,
    val measuredAt: LocalDateTime = LocalDateTime.now()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WeightMeasurement

        if (measuredAt != other.measuredAt) return false
        return value == other.value
    }

    override fun hashCode(): Int {
        var result = measuredAt.hashCode()
        result = 31 * result + value
        return result
    }
}
