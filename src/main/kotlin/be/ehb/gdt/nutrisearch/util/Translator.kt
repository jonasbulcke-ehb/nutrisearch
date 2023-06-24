@file:JvmName("Translator")
package be.ehb.gdt.nutrisearch.util

import be.ehb.gdt.nutrisearch.domain.consumption.valueobjects.Meal
import be.ehb.gdt.nutrisearch.domain.product.valueobjects.Nutrient

@SuppressWarnings("kotlin:S1479")
fun getAbbreviation(nutrient: Nutrient): String {
    return when(nutrient) {
        Nutrient.Energy -> "Energie"
        Nutrient.Protein -> "Eiwitten"
        Nutrient.Fat -> "Vetten"
        Nutrient.SaturatedFattyAcid -> "Verzadigde vetten"
        Nutrient.PolyUnsaturatedFattyAcid -> "Meerv. Onv. Vetten"
        Nutrient.MonoUnsaturatedFattyAcid -> "Enkelv. Onv. Vetten"
        Nutrient.Omega3FattyAcid -> "Omega-3-vetzuren"
        Nutrient.Omega6FattyAcid -> "Omega-6-vetzuren"
        Nutrient.LinoleicAcid -> "Linolzuur"
        Nutrient.TransFattyAcid -> "Transvetzuren"
        Nutrient.Cholesterol -> "Cholesterol"
        Nutrient.Carbohydrates -> "Koolhydraten"
        Nutrient.Sugar -> "Suiker"
        Nutrient.Starch -> "Zetmeel"
        Nutrient.Fiber -> "Vezels"
        Nutrient.Water -> "Water"
        Nutrient.Sodium -> "Natrium"
        Nutrient.Potassium -> "Kalium"
        Nutrient.Calcium -> "Calcium"
        Nutrient.Phosphor -> "Fosfor"
        Nutrient.Magnesium -> "Magnesium"
        Nutrient.Iron -> "Ijzer"
        Nutrient.Copper -> "Koper"
        Nutrient.Zinc -> "Zink"
        Nutrient.Iodine -> "Jodium"
        Nutrient.Selenium -> "Selenium"
        Nutrient.VitaminA -> "Vit. A"
        Nutrient.VitaminB1 -> "Vit. B1"
        Nutrient.VitaminB2 -> "Vit. B2"
        Nutrient.VitaminB12 -> "Vit. B12"
        Nutrient.Folate -> "Folaat"
        Nutrient.VitaminC -> "Vit. C"
        Nutrient.VitaminD -> "Vit. D"
        Nutrient.VitaminE -> "Vit. E"
    }
}

fun getMealName(meal: Meal): String {
    return when(meal) {
        Meal.Breakfast -> "Ontbijt"
        Meal.Lunch -> "Middagmaal"
        Meal.Dinner -> "Avondmaal"
        Meal.Snacks -> "Tussendoor"
    }
}