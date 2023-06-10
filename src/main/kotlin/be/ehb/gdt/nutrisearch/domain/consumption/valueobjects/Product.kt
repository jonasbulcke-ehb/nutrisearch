package be.ehb.gdt.nutrisearch.domain.consumption.valueobjects

class Product(val name: String, val id: String) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Product

        if (name != other.name) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + id.hashCode()
        return result
    }
}