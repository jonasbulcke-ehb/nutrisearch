package be.ehb.gdt.nutrisearch.domain.product.valueobjects

class ServingSize(val grams: Int, val name: String) {

    constructor() : this(1, "Gram")

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ServingSize

        if (grams != other.grams) return false
        return name == other.name
    }

    override fun hashCode(): Int {
        var result = grams.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }


}