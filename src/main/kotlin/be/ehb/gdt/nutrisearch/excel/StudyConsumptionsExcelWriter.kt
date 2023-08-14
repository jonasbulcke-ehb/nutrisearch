package be.ehb.gdt.nutrisearch.excel

import be.ehb.gdt.nutrisearch.domain.consumption.entities.Consumption
import be.ehb.gdt.nutrisearch.domain.product.valueobjects.Nutrient
import be.ehb.gdt.nutrisearch.domain.userinfo.entities.UserInfo
import be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects.Sex
import be.ehb.gdt.nutrisearch.util.booleanToYesNo
import be.ehb.gdt.nutrisearch.util.getActivityLevel

class StudyConsumptionsExcelWriter(
    private val consumptions: Map<UserInfo, List<Consumption>>,
    sheetName: String? = null
) :
    ConsumptionsExcelWriter(sheetName) {
    override fun fillWorkbook() {
        addHeaders(Nutrient.values().asList())
        consumptions.forEach { addUserConsumptionsRow(it.key, it.value) }

        IntRange(0, 43).forEach(sheet::autoSizeColumn)
    }

    private fun addUserConsumptionsRow(userInfo: UserInfo, consumptions: List<Consumption>) {
        val amountsByNutrients = consumptions
            .flatMap { it.preparation.nutrients.entries }
            .groupBy({ it.key }, { it.value })

        addTotalRow(amountsByNutrients, Nutrient.values().toList(), centerCellStyle)
        writeUserInfo(userInfo)
    }

    override fun addHeaders(nutrients: List<Nutrient>) {
        super.addHeaders(nutrients)
        var cellIndex = Nutrient.values().size + 1
        sheet.getRow(rowIndex - 1).apply {
            for (i in 0 until 8) {
                createCell(cellIndex + i).apply {
                    cellStyle = headerUnitCellStyle
                }
            }
            getCell(cellIndex + 2).apply {
                setCellValue("cm")
            }
            getCell(cellIndex + 3).apply {
                setCellValue("kg")
            }
            getCell(cellIndex + 4).apply {
                setCellValue("kg/m^2")
            }
        }
        sheet.getRow(rowIndex - 2).apply {
            listOf(
                "Geboortedatum",
                "Bewegingsniveau",
                "Lengte",
                "Gewicht",
                "BMI",
                "Geslacht",
                "Zwanger",
                "Borstvoeding"
            ).forEach {
                createCell(cellIndex++).apply {
                    setCellValue(it)
                    cellStyle = headerTitleCellStyle
                }
            }
        }
    }

    private fun writeUserInfo(userInfo: UserInfo) {
        var cellIndex = Nutrient.values().size + 1
        sheet.getRow(rowIndex - 1).apply {
            createCell(cellIndex++).apply {
                setCellValue(userInfo.dob)
                workbook
                cellStyle = centerCellStyle.copy().apply {
                    dataFormat = workbook.creationHelper.createDataFormat().getFormat("dd/MM/yyyy")
                }

            }
            createCell(cellIndex++).apply {
                setCellValue(getActivityLevel(userInfo.activityLevel))
                cellStyle = centerCellStyle
            }
            createCell(cellIndex++).apply {
                setCellValue(userInfo.length.toDouble())
                cellStyle = centerCellStyle
            }
            createCell(cellIndex++).apply {
                setCellValue(userInfo.weightMeasurements.last().value)
                cellStyle = centerCellStyle
            }
            sheet.setColumnWidth(cellIndex, 4)
            createCell(cellIndex++).apply {
                cellFormula = "\$AM${rowIndex + 1}/(\$AL${rowIndex + 1}/100)^2"
                cellStyle = centerCellStyle.copy().apply {
                    dataFormat = workbook.creationHelper.createDataFormat().getFormat("#.#")
                }
            }
            createCell(cellIndex++).apply {
                setCellValue(if (userInfo.sex == Sex.Male) "Mannelijk" else "Vrouwelijk")
                cellStyle = centerCellStyle
            }
            createCell(cellIndex++).apply {
                setCellValue(booleanToYesNo(userInfo.isPregnant))
                cellStyle = centerCellStyle
            }
            createCell(cellIndex++).apply {
                setCellValue(booleanToYesNo(userInfo.isBreastfeeding))
                cellStyle = centerCellStyle
            }
        }
    }
}