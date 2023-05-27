package be.ehb.gdt.nutrisearch.domain.product.valueobjects

enum class Nutrient(
    val unit: String = "g",
    val isMacroNutrient: Boolean = true
) {
    Energy("kcal"),
    Protein,
    Fat,
    SaturatedFattyAcid,
    PolyUnsaturatedFattyAcid,
    MonoUnsaturatedFattyAcid,
    Omega3FattyAcid,
    Omega6FattyAcid,
    LinoleicAcid,
    TransFattyAcid,
    Cholesterol("mg"),
    Carbohydrates,
    Sugar,
    Starch,
    Fiber,
    Water,
    Sodium(false),
    Potassium(false),
    Calcium(false),
    Phosphor(false),
    Magnesium(false),
    Iron(false),
    Copper(false),
    Zinc(false),
    Iodine("µg", false),
    Selenium("µg", false),
    VitaminA(false),
    VitaminB1(false),
    VitaminB2(false),
    VitaminB12("µg", false),
    Folate("µg", false),
    VitaminC(false),
    VitaminD("µg", false),
    VitaminE("µg", false);

    constructor(isMacroNutrient: Boolean) : this("mg", isMacroNutrient)
}