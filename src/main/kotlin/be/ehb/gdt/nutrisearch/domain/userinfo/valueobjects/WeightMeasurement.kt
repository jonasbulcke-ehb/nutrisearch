package be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects

import java.time.LocalDateTime

class WeightMeasurement(
    val value: Double,
    val timestamp: LocalDateTime = LocalDateTime.now()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WeightMeasurement

        if (timestamp != other.timestamp) return false
        return value == other.value
    }

    override fun hashCode(): Int {
        var result = value.hashCode()
        result = 31 * result + timestamp.hashCode()
        return result
    }


}
