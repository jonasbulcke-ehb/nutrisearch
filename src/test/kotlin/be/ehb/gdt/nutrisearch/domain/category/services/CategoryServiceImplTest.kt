package be.ehb.gdt.nutrisearch.domain.category.services

import be.ehb.gdt.nutrisearch.domain.category.entities.Category
import be.ehb.gdt.nutrisearch.domain.category.entities.Subcategory
import be.ehb.gdt.nutrisearch.domain.category.exceptions.CategoryNotFoundException
import be.ehb.gdt.nutrisearch.domain.category.repositories.CategoryRepository
import org.bson.types.ObjectId
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.inOrder
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class CategoryServiceImplTest {
    @Mock
    private lateinit var repo: CategoryRepository

    private lateinit var service: CategoryService
    private val id = "categoryId"
    private val name = "Groenten & Fruit"
    private val category = Category(
        name,
        mutableListOf(Subcategory("Groenten"), Subcategory("Fuit"), Subcategory("Noten")),
        id
    )

    @BeforeEach
    fun setUp() {
        service = CategoryServiceImpl(repo)
    }

    @Test
    fun `when the categories exist then the list should be returned`() {
        whenever(repo.findAllCategories())
            .thenReturn(
                listOf(
                    category,
                    Category("Droogwaren")
                )
            )
        val subcategories = service.getCategories()
        assertEquals(2, subcategories.size)
        verify(repo).findAllCategories()
    }

    @Test
    fun `when the category exists then it should be returned`() {
        val id = ObjectId.get().toHexString()
        whenever(repo.findCategory(id)).thenReturn(Category(name, mutableListOf(), id))

        val category = service.getCategory(id)
        assertEquals(Category(name, mutableListOf(), id), category)
        verify(repo).findCategory(id)
    }

    @Test
    fun `when category does not exist then throw exception`() {
        whenever(repo.findCategory(id)).thenReturn(null)

        val exception = assertThrows(CategoryNotFoundException::class.java) { service.getCategory(id) }
        assertEquals("Category with identifier $id could not be found", exception.message)
        verify(repo).findCategory(id)
    }

    @Test
    fun `when category does not exist then add category`() {
        whenever(repo.insertCategory(any())).thenReturn(Category(name))

        val createdCategory = service.createCategory(name)
        assertEquals(name, createdCategory.name)
        verify(repo).insertCategory(any())
    }

    @Test
    fun `when category does not exists and tries to update then throw exception`() {
        whenever(repo.existsCategoryById(id)).thenReturn(false)

        val exception = assertThrows(CategoryNotFoundException::class.java) { service.updateCategory(id, name) }
        assertEquals("Category with identifier $id could not be found", exception.message)
        verify(repo).existsCategoryById(id)
    }

    @Test
    fun `when category exists and tries to update then update`() {
        whenever(repo.existsCategoryById(id)).thenReturn(true)

        assertDoesNotThrow { service.updateCategory(id, name) }

        val inOrder = inOrder(repo)
        inOrder.verify(repo).existsCategoryById(id)
        inOrder.verify(repo).updateCategory(id, name)
    }

    @Test
    fun `when category does not exists and tries to delete then throw exception`() {
        whenever(repo.existsCategoryById(id)).thenReturn(false)

        val exception = assertThrows(CategoryNotFoundException::class.java) { service.deleteCategory(id) }
        assertEquals("Category with identifier $id could not be found", exception.message)
        verify(repo).existsCategoryById(id)
    }

    @Test
    fun `when category exists and tries to delete then delete`() {
        whenever(repo.existsCategoryById(id)).thenReturn(true)

        assertDoesNotThrow { service.deleteCategory(id) }
        val inOrder = inOrder(repo)
        inOrder.verify(repo).existsCategoryById(id)
        inOrder.verify(repo).deleteCategory(id)
    }
}