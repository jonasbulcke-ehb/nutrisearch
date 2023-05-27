package be.ehb.gdt.nutrisearch.domain.product.valueobjects

class ServingSize(val grams: Double, val name: String) {

    constructor() : this(1.0, "Gram")
}