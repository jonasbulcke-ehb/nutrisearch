package be.ehb.gdt.nutrisearch.excel

import be.ehb.gdt.nutrisearch.domain.consumption.entities.Consumption
import be.ehb.gdt.nutrisearch.domain.product.valueobjects.Nutrient
import be.ehb.gdt.nutrisearch.domain.questionnaire.valueobjects.Answer
import be.ehb.gdt.nutrisearch.domain.userinfo.entities.UserInfo
import be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects.Sex
import be.ehb.gdt.nutrisearch.util.booleanToYesNo
import be.ehb.gdt.nutrisearch.util.getActivityLevel
import be.ehb.gdt.nutrisearch.util.getMealName
import java.time.LocalDate

class StudyConsumptionsExcelWriter(
    private val consumptions: Map<UserInfo, List<Consumption>>,
    private val answers: Map<String, List<Answer>>,
    private val date: LocalDate,
) : ConsumptionsExcelWriter(date.toString()) {

    private val answersLength: Int by lazy {
        answers.values.first().size
    }

    override fun fillWorkbook() {
        addHeaders(Nutrient.values().asList())
        consumptions.forEach { addUserConsumptionsRow(it.key, it.value) }

        IntRange(0, 43 + answersLength).forEach(sheet::autoSizeColumn)
    }

    private fun addUserConsumptionsRow(userInfo: UserInfo, consumptions: List<Consumption>) {
        val amountsByNutrients = consumptions
            .flatMap { it.preparation.nutrients.entries }
            .groupBy({ it.key }, { it.value })

        addTotalRow(amountsByNutrients, Nutrient.values().toList(), centerCellStyle)
        writeUserInfo(userInfo)
        writeAnswers(answers[userInfo.id] ?: listOf())
    }

    override fun addHeaders(nutrients: List<Nutrient>) {
        super.addHeaders(nutrients)
        var cellIndex = Nutrient.values().size + 1
        val answers = answers.values.first().sorted()
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
            answers.withIndex().forEach { (i, answer) ->
                createCell(cellIndex + 8 + i).apply {
                    setCellValue(answer.question.options?.joinToString("/"))
                    cellStyle = headerUnitCellStyle
                }
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
            answers.forEach {
                createCell(cellIndex++).apply {
                    setCellValue(it.question.question + (it.meal?.let { meal -> " (${getMealName(meal)})" } ?: ""))
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
                userInfo.getWeight(date)?.let { setCellValue(it) }
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

    private fun writeAnswers(answers: List<Answer>) {
        val startCellIndex = Nutrient.values().size + 9
        val sortedAnswers = answers.sorted()
        sheet.getRow(rowIndex - 1).apply {
            for (i in 0 until answersLength) {
                createCell(startCellIndex + i).apply {
                    setCellValue(sortedAnswers.getOrNull(i)?.answer)
                    cellStyle = centerCellStyle
                }
            }

        }
    }
}