package be.ehb.gdt.nutrisearch.domain.consumption.services

import be.ehb.gdt.nutrisearch.domain.consumption.entities.Consumption
import be.ehb.gdt.nutrisearch.domain.consumption.valueobjects.Meal
import be.ehb.gdt.nutrisearch.domain.product.valueobjects.Nutrient
import be.ehb.gdt.nutrisearch.util.getAbbreviation
import be.ehb.gdt.nutrisearch.util.getMealName
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.OutputStream

class IndividualConsumptionsExcelWriter(private val consumptions: List<Consumption>, sheetName: String) {
    private val workbook: XSSFWorkbook = XSSFWorkbook()
    private val sheet: XSSFSheet
    private val centerCellStyle: XSSFCellStyle
    private val boldCellStyle: XSSFCellStyle
    private val subtitleCellStyle: XSSFCellStyle
    private var rowIndex: Int = 0

    init {
        sheet = workbook.createSheet(sheetName)
        centerCellStyle = workbook.createCellStyle().apply {
            alignment = HorizontalAlignment.CENTER
            borderBottom = BorderStyle.THIN
            borderTop = BorderStyle.THIN
        }
        boldCellStyle = workbook.createCellStyle().apply {
            setFont(workbook.createFont().apply {
                bold = true
            })
            borderTop = BorderStyle.MEDIUM
            borderBottom = BorderStyle.THIN
        }
        subtitleCellStyle = workbook.createCellStyle().apply {
            setFont(workbook.createFont().apply {
                bold = true
                setFontHeight(16.0)
                borderBottom = BorderStyle.MEDIUM
                borderTop = BorderStyle.MEDIUM
            })
            alignment = HorizontalAlignment.CENTER
        }
    }

    fun write(outputStream: OutputStream) {
        addSubtitle("Macronutriënten", Nutrient.macroNutrients.size)
        addHeaders(Nutrient.macroNutrients)
        addConsumptionsByNutrients(Nutrient.macroNutrients)
        addTotalRow(Nutrient.macroNutrients)
        rowIndex += 2
        addSubtitle("Micronutriënten", Nutrient.microNutrients.size)
        addHeaders(Nutrient.microNutrients)
        addConsumptionsByNutrients(Nutrient.microNutrients)
        addTotalRow(Nutrient.microNutrients)

        IntRange(0, 18).forEach(sheet::autoSizeColumn)

        workbook.write(outputStream)
        workbook.close()
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

    private fun addHeaders(nutrients: List<Nutrient>) {
        sheet.createRow(rowIndex).also {
            for (i in nutrients.indices) {
                it.createCell(i + 1).apply {
                    setCellValue(getAbbreviation(nutrients[i]))
                    cellStyle = workbook.createCellStyle().apply {
                        alignment = HorizontalAlignment.CENTER
                        borderBottom = BorderStyle.THIN
                    }
                }
            }
        }
        sheet.createRow(rowIndex + 1).also {
            it.createCell(0).apply {
                setCellValue("Eenheid")
                cellStyle = workbook.createCellStyle().apply {
                    setFont(workbook.createFont().apply {
                        bold = true
                    })
                    borderTop = BorderStyle.THIN
                }
            }
            for (i in nutrients.indices) {
                it.createCell(i + 1).apply {
                    setCellValue(nutrients[i].unit)
                    cellStyle = workbook.createCellStyle().apply {
                        alignment = HorizontalAlignment.CENTER
                        borderBottom = BorderStyle.MEDIUM
                    }
                }
            }
        }
        rowIndex += 2
    }

    private fun addConsumptionsByNutrients(nutrients: List<Nutrient>) {
        val groupedConsumptions = consumptions.groupBy { it.meal }
        for (i in 0 until groupedConsumptions.size) {
            val meal = Meal.values()[i]
            writeMealRow(meal, nutrients.size)
            for (consumption in groupedConsumptions[meal] ?: listOf()) {
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
                cellStyle = workbook.createCellStyle().apply {
                    borderBottom = BorderStyle.THIN
                }
            }
            var cellCount = 1;
            for (nutrient in nutrients) {
                it.createCell(cellCount).apply {
                    val amount = consumption.preparation.nutrients[nutrient]
                    if (amount == null) {
                        setCellValue(" - ")
                    } else {
                        setCellValue(amount.toDouble())
                    }
                    cellStyle = centerCellStyle
                }
                cellCount++
            }
            rowIndex++
        }
    }

    private fun addTotalRow(nutrients: List<Nutrient>) {
        val style = centerCellStyle.copy().apply {
            borderTop = BorderStyle.MEDIUM
        }
        val amountsByNutrients = consumptions
            .flatMap { it.preparation.nutrients.entries }
            .groupBy({ it.key }, { it.value })


        sheet.createRow(rowIndex).apply {
            createCell(0).apply {
                setCellValue("Dagtotaal")
                cellStyle = boldCellStyle
            }
            for (i in nutrients.indices) {
                val sum = amountsByNutrients[nutrients[i]]!!.map { it ?: 0 }.reduce { a, b -> a + b }.toDouble()
                createCell(i + 1).apply {
                    setCellValue(sum)
                    cellStyle = style
                }
            }
        }
        rowIndex++
    }
}