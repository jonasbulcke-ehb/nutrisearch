package be.ehb.gdt.nutrisearch.excel

import be.ehb.gdt.nutrisearch.domain.product.valueobjects.Nutrient
import be.ehb.gdt.nutrisearch.util.getAbbreviation
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.OutputStream

abstract class ConsumptionsExcelWriter(sheetName: String? = null) {
    protected var rowIndex = 0

    protected val workbook: XSSFWorkbook = XSSFWorkbook()
    protected val sheet: XSSFSheet
    protected val centerCellStyle: XSSFCellStyle
    protected val boldCellStyle: XSSFCellStyle
    protected val subtitleCellStyle: XSSFCellStyle
    protected val headerTitleCellStyle: XSSFCellStyle
    protected val headerUnitCellStyle: XSSFCellStyle

    init {
        sheet = sheetName?.let { workbook.createSheet(it) } ?: workbook.createSheet()
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
        headerTitleCellStyle = workbook.createCellStyle().apply {
            alignment = HorizontalAlignment.CENTER
            borderBottom = BorderStyle.THIN
        }
        headerUnitCellStyle = workbook.createCellStyle().apply {
            alignment = HorizontalAlignment.CENTER
            borderBottom = BorderStyle.MEDIUM
        }
    }

    protected abstract fun fillWorkbook()

    fun write(outputStream: OutputStream) {
        fillWorkbook()
        workbook.write(outputStream)
    }

    protected open fun addHeaders(nutrients: List<Nutrient>) {
        sheet.createRow(rowIndex).also {
            for (i in nutrients.indices) {
                it.createCell(i + 1).apply {
                    setCellValue(getAbbreviation(nutrients[i]))
                    cellStyle = headerTitleCellStyle
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
                    cellStyle = headerUnitCellStyle
                }
            }
        }
        rowIndex += 2
    }

    protected open fun addTotalRow(
        amountsByNutrients: Map<Nutrient, List<Double?>>,
        nutrients: List<Nutrient>,
        style: XSSFCellStyle
    ) {
        sheet.createRow(rowIndex).apply {
            createCell(0).apply {
                setCellValue("Dagtotaal")
                cellStyle = boldCellStyle.copy().apply {
                    borderBottom = style.borderBottom
                    if (rowIndex != 2) {
                        borderTop = style.borderBottom
                    }
                }
            }
            for (i in nutrients.indices) {
                val sum = amountsByNutrients[nutrients[i]]?.map { it ?: 0.0 }?.reduce { a, b -> a + b }
                createCell(i + 1).apply {
                    setCellValue(sum ?: 0.0)
                    cellStyle = style
                }
            }
        }
        rowIndex++
    }
}