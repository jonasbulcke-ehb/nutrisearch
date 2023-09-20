package be.ehb.gdt.nutrisearch.domain.category.services

import be.ehb.gdt.nutrisearch.domain.category.entities.Subcategory
import be.ehb.gdt.nutrisearch.domain.exceptions.ResourceNotFoundException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*

@ExtendWith(MockitoExtension::class)
class SubcategoryServiceImplTest {
    @Mock
    lateinit var repo: SubcategoryRepository

    private lateinit var service: SubcategoryServiceImpl
    private val parentId = "parent-category-id"
    private val id = "subcategoryId"
    private val name = "Brood"
    private val subcategory = Subcategory(name, id)


    @BeforeEach
    fun setUp() {
        service = SubcategoryServiceImpl(repo)
    }

    @Nested
    inner class ParentCategoryDoesNotExist {
        @BeforeEach
        fun setUp() {
            whenever(repo.existsParentCategoryById(parentId)).thenReturn(false)
        }

        @Test
        fun `when get subcategories then throw exception`() {
            val exception = assertThrows(ResourceNotFoundException::class.java) { service.getSubcategories(parentId) }
            assertEquals("Resource of type Category with id $parentId could not be found", exception.message)
            verify(repo).existsParentCategoryById(parentId)
            verifyNoMoreInteractions(repo)
        }

        @Test
        fun `when subcategory exists and get subcategory then throw exception`() {
            val exception = assertThrows(ResourceNotFoundException::class.java) { service.getSubcategory(parentId, id) }
            assertEquals("Resource of type Category with id $parentId could not be found", exception.message)
            verify(repo).existsParentCategoryById(parentId)
            verifyNoMoreInteractions(repo)
        }

        @Test
        fun `when subcategory does not exist and get subcategory then throw exception`() {
            val exception = assertThrows(ResourceNotFoundException::class.java) { service.getSubcategory(parentId, id) }
            assertEquals("Resource of type Category with id $parentId could not be found", exception.message)
            verify(repo).existsParentCategoryById(parentId)
            verifyNoMoreInteractions(repo)
        }

        @Test
        fun `when create subcategory then throw exception`() {
            val exception =
                assertThrows(ResourceNotFoundException::class.java) { service.createSubcategory(parentId, name) }
            assertEquals("Resource of type Category with id $parentId could not be found", exception.message)
            verify(repo).existsParentCategoryById(parentId)
            verifyNoMoreInteractions(repo)
        }
    }

    @Nested
    inner class SubcategoryDoesNotExist {
        @BeforeEach
        fun setUp() {
            whenever(repo.existsSubcategoryById(parentId, id)).thenReturn(false)
        }

        @Test
        fun `when subcategory does not exists and update subcategory then throw exception`() {
            val exception =
                assertThrows(ResourceNotFoundException::class.java) { service.updateSubcategory(parentId, id, name) }
            assertEquals(
                "Resource of type Subcategory with id $id could not be found",
                exception.message
            )
            verify(repo).existsSubcategoryById(parentId, id)
        }

        @Test
        fun `when subcategory does not exists and delete subcategory then throw exception`() {
            val exception =
                assertThrows(ResourceNotFoundException::class.java) { service.deleteSubcategory(parentId, id) }
            assertEquals(
                "Resource of type Subcategory with id $id could not be found",
                exception.message
            )
            verify(repo).existsSubcategoryById(parentId, id)
        }
    }

    @Nested
    inner class ParentCategoryExists {
        @BeforeEach
        fun setUp() {
            whenever(repo.existsParentCategoryById(parentId)).thenReturn(true)
        }

        @Test
        fun `when get subcategories then list is returned`() {
            whenever(repo.findAllSubcategories(parentId))
                .thenReturn(
                    listOf(
                        subcategory,
                        Subcategory("Cornflakes")
                    )
                )
            val subcategories = service.getSubcategories(parentId)
            assertEquals(2, subcategories.size)
            inOrder(repo).apply {
                verify(repo).existsParentCategoryById(parentId)
                verify(repo).findAllSubcategories(parentId)
            }
        }

        @Test
        fun `when get subcategory then return subcategory`() {
            whenever(repo.findSubcategory(parentId, id)).thenReturn(Subcategory(name, id))

            val subcategory = service.getSubcategory(parentId, id)
            assertEquals(Subcategory(name, id), subcategory)
            inOrder(repo).apply {
                verify(repo).existsParentCategoryById(parentId)
                verify(repo).findSubcategory(parentId, id)
            }
        }


        @Test
        fun `when get subcategory then throw exception`() {
            whenever(repo.findSubcategory(parentId, id)).thenReturn(null)

            val exception =
                assertThrows(ResourceNotFoundException::class.java) { service.getSubcategory(parentId, id) }
            assertEquals(
                "Resource of type Subcategory with id $id could not be found",
                exception.message
            )
            inOrder(repo).apply {
                verify(repo).existsParentCategoryById(parentId)
                verify(repo).findSubcategory(parentId, id)
            }
        }

        @Test
        fun `when create subcategory then create subcategory`() {
            whenever(repo.insertSubcategory(eq(parentId), any())).thenReturn(Subcategory(name))

            val createdSubcategory = service.createSubcategory(parentId, name)
            assertEquals(name, createdSubcategory.name)
            verify(repo).insertSubcategory(eq(parentId), any())
        }
    }

    @Nested
    inner class SubcategoryExists {
        @BeforeEach
        fun setUp() {
            whenever(repo.existsSubcategoryById(parentId, id)).thenReturn(true)
        }

        @Test
        fun `when update subcategory then update subcategory`() {
            assertDoesNotThrow { service.updateSubcategory(parentId, id, name) }
            inOrder(repo).apply {
                verify(repo).existsSubcategoryById(parentId, id)
                verify(repo).updateSubcategory(parentId, Subcategory(name, id))
            }
        }

        @Test
        fun `when delete only subcategory then throw exception`() {
            assertThrows(IllegalStateException::class.java) { service.deleteSubcategory(parentId, id) }
            inOrder(repo).apply {
                verify(repo).existsSubcategoryById(parentId, id)
                verify(repo).countSubcategoriesByCategoryId(parentId)
            }
        }

        @Test
        fun `when delete subcategory then delete subcategory`() {
            whenever(repo.countSubcategoriesByCategoryId(parentId)).thenReturn(2)
            assertDoesNotThrow { service.deleteSubcategory(parentId, id) }
            inOrder(repo).apply {
                verify(repo).existsSubcategoryById(parentId, id)
                verify(repo).countSubcategoriesByCategoryId(parentId)
                verify(repo).deleteSubcategory(parentId, id)
            }
        }
    }
}