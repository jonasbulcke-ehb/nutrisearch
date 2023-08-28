package be.ehb.gdt.nutrisearch.excel

import be.ehb.gdt.nutrisearch.domain.product.entities.Preparation
import be.ehb.gdt.nutrisearch.domain.product.entities.Product
import be.ehb.gdt.nutrisearch.domain.product.valueobjects.Nutrient
import be.ehb.gdt.nutrisearch.domain.product.valueobjects.ServingSize
import be.ehb.gdt.nutrisearch.excel.exceptions.InvalidExcelException
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.InputStream

class ProductsExcelReader(
    private val workbook: XSSFWorkbook,
    private val nutrientColumnIndices: Map<Nutrient, Int>,
    private val nameColumnIndex: Int,
    private val brandColumnIndex: Int?
) {
    lateinit var categoryId: String
    var isVerified: Boolean = false
    var servingSizes: MutableSet<ServingSize> = mutableSetOf()

    fun witProductDetails(
        categoryId: String,
        isVerified: Boolean = false,
        vararg servingSizes: ServingSize
    ): ProductsExcelReader {
        this.categoryId = categoryId
        this.isVerified = isVerified
        this.servingSizes.addAll(servingSizes)
        return this
    }

    fun readProducts(): List<Product> {
        check(this::categoryId.isInitialized) { "Category id is not initialized" }
        return workbook.getSheetAt(0).toList().drop(1).map {
            readProduct(it)
        }
    }

    private fun readProduct(row: Row): Product {
        val name = row.getCell(nameColumnIndex).stringCellValue
        val brand = brandColumnIndex?.let { row.getCell(it)?.stringCellValue }
        val nutrients: MutableMap<Nutrient, Double?> = Nutrient.values().associateWith { null }.toMutableMap()
        nutrientColumnIndices.forEach { (nutrient, index) ->
            if (row.getCell(index)?.cellType == CellType.NUMERIC) {
                nutrients[nutrient] = row.getCell(index).numericCellValue
            }
        }
        return Product(
            brand,
            name,
            categoryId,
            isVerified,
            mutableListOf(Preparation("Onbereid", nutrients)),
            servingSizes
        )
    }


    companion object {
        fun from(inputStream: InputStream): ProductsExcelReader {
            val workbook = XSSFWorkbook(inputStream)
            val nutrientColumnIndices: Map<Nutrient, Int>
            val nameColumnIndex: Int
            val brandColumnIndex: Int?
            workbook.getSheetAt(0).rowIterator().next().also { row ->
                nutrientColumnIndices = nutrientsRowToIndicesMap(row)
                nameColumnIndex = row.firstOrNull { cell -> cell.stringCellValue == "naam" }?.columnIndex ?: throw InvalidExcelException()
                brandColumnIndex = row.firstOrNull { cell -> cell.stringCellValue == "merk" }?.columnIndex
            }

            return ProductsExcelReader(workbook, nutrientColumnIndices, nameColumnIndex, brandColumnIndex)
        }

        private fun nutrientsRowToIndicesMap(row: Row): Map<Nutrient, Int> {
            val map: MutableMap<Nutrient, Int> = mutableMapOf();
            row.cellIterator().forEach { cell ->
                val nutrient: Nutrient? = when (cell.stringCellValue.lowercase()) {
                    "energie" -> Nutrient.Energy
                    "eiwitten" -> Nutrient.Protein
                    "vetten", "vet" -> Nutrient.Fat
                    "verzadigde vetten", "verzadigde vetzuren" -> Nutrient.SaturatedFattyAcid
                    "enkelv. onv. vetzuren", "enkelvoudige onverzadigde vetzuren", "enkelv. onv. vetten", "enkelvoudige onverzadigde vetten" -> Nutrient.MonoUnsaturatedFattyAcid
                    "meerv. onv. vetzuren", "meervoudige onverzadigde vetzuren", "meerv. onv. vetten", "meervoudige onverzadigde vetten" -> Nutrient.PolyUnsaturatedFattyAcid
                    "omega-3-vetzuren", "omega 3 vetzuren", "omega-3-vetten", "omega 3 vetten" -> Nutrient.Omega3FattyAcid
                    "omega-6-vetzuren", "omega 6 vetzuren", "omega-6-vetten", "omega 6 vetten" -> Nutrient.Omega6FattyAcid
                    "linolzuur" -> Nutrient.LinoleicAcid
                    "transvetzuren", "transvetten" -> Nutrient.TransFattyAcid
                    "cholesterol" -> Nutrient.Cholesterol
                    "koolhydraten" -> Nutrient.Carbohydrates
                    "suikers", "suiker" -> Nutrient.Sugar
                    "zetmeel", "zetmelen" -> Nutrient.Starch
                    "vezels" -> Nutrient.Fiber
                    "water" -> Nutrient.Water
                    "natrium" -> Nutrient.Sodium
                    "kalium" -> Nutrient.Potassium
                    "calcium" -> Nutrient.Calcium
                    "fosfor" -> Nutrient.Phosphor
                    "magnesium" -> Nutrient.Magnesium
                    "ijzer" -> Nutrient.Iron
                    "koper" -> Nutrient.Copper
                    "zink" -> Nutrient.Zinc
                    "selenium" -> Nutrient.Selenium
                    "vit. a", "vit a", "vitamine a" -> Nutrient.VitaminA
                    "vit. b1", "vit b1", "vitamine b1" -> Nutrient.VitaminB1
                    "vit. b2", "vit b2", "vitamine b2" -> Nutrient.VitaminB2
                    "vit. b12", "vit b12", "vitamine b12" -> Nutrient.VitaminB12
                    "vit. c", "vit c", "vitamine c" -> Nutrient.VitaminC
                    "vit. d", "vit d", "vitamine d" -> Nutrient.VitaminD
                    "vit. e", "vit e", "vitamine e" -> Nutrient.VitaminE
                    "foliumzuur", "folaat" -> Nutrient.Folate
                    "jodium" -> Nutrient.Iodine
                    else -> null
                }
                nutrient?.let { map[it] = cell.columnIndex }
            }
            return map
        }

    }

}