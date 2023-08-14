package be.ehb.gdt.nutrisearch.excel

import be.ehb.gdt.nutrisearch.domain.consumption.entities.Consumption
import be.ehb.gdt.nutrisearch.domain.consumption.valueobjects.Meal
import be.ehb.gdt.nutrisearch.domain.product.valueobjects.Nutrient
import be.ehb.gdt.nutrisearch.util.getMealName
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.util.CellRangeAddress

class UserConsumptionsExcelWriter(
    private val consumptions: List<Consumption>,
    sheetName: String? = null
) : ConsumptionsExcelWriter(sheetName) {

    override fun fillWorkbook() {
        addSubtitle("Macronutriënten", Nutrient.macroNutrients.size)
        addHeaders(Nutrient.macroNutrients)
        addConsumptionsByNutrients(Nutrient.macroNutrients)
        addSumRow(Nutrient.macroNutrients.size)
        rowIndex += 2
        addSubtitle("Micronutriënten", Nutrient.microNutrients.size)
        addHeaders(Nutrient.microNutrients)
        addConsumptionsByNutrients(Nutrient.microNutrients)
        addSumRow(Nutrient.microNutrients.size)

        IntRange(0, 18).forEach(sheet::autoSizeColumn)
    }

    private fun addSubtitle(title: String, length: Int) {
        sheet.addMergedRegion(CellRangeAddress(rowIndex, rowIndex, 0, length))
        sheet.createRow(rowIndex).apply {
            createCell(0).apply {
                setCellValue(title)
                cellStyle = subtitleCellStyle
            }
            for (i in 0 until length) {
                createCell(i + 1).cellStyle = subtitleCellStyle
            }
        }
        rowIndex++
    }

    private fun addConsumptionsByNutrients(nutrients: List<Nutrient>) {
        val groupedConsumptions = consumptions.groupBy { it.meal }
        for (meal in Meal.values()) {
            writeMealRow(meal, nutrients.size)
            for (consumption in groupedConsumptions.getOrDefault(meal, listOf())) {
                writeConsumptionRow(consumption, nutrients)
            }
        }
    }

    private fun writeMealRow(meal: Meal, length: Int) {
        sheet.createRow(rowIndex).also {
            it.createCell(0).apply {
                setCellValue(getMealName(meal))
                cellStyle = boldCellStyle
            }
            for (j in 0 until length) {
                it.createCell(j + 1).apply {
                    cellStyle = boldCellStyle
                }
            }
            rowIndex++
        }
    }

    private fun writeConsumptionRow(consumption: Consumption, nutrients: List<Nutrient>) {
        sheet.createRow(rowIndex).also {
            it.createCell(0).apply {
                setCellValue("${consumption.product.name}, ${consumption.amount * consumption.servingSize.grams} g")
                cellStyle = workbook.createCellStyle().apply { borderBottom = BorderStyle.THIN }
            }
            var cellCount = 1
            for (nutrient in nutrients) {
                it.createCell(cellCount).apply {
                    val unityAmount = consumption.preparation.nutrients[nutrient]
                    if (unityAmount == null) {
                        setCellValue(" - ")
                    } else {
                        val amount = unityAmount * consumption.servingSize.grams * consumption.amount / 100
                        setCellValue(amount)
                    }
                    cellStyle = centerCellStyle
                }
                cellCount++
            }
            rowIndex++
        }
    }

    private fun addSumRow(length: Int) {
        sheet.createRow(rowIndex).apply {
            createCell(0).apply {
                setCellValue("Dagtotaal")
                cellStyle = boldCellStyle
            }
            for(i in 1..length) {
                val start = rowIndex - 3 - consumptions.size
                val end = rowIndex
                val letter = 'A'.plus(i)
                createCell(i).apply {
                    cellFormula = "SUM(${letter}${start}:${letter}${end})"
                    cellStyle = centerCellStyle
                }
            }
            rowIndex++
        }
    }
}