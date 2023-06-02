package be.ehb.gdt.nutrisearch.restapi.category

import be.ehb.gdt.nutrisearch.domain.category.entities.Category
import be.ehb.gdt.nutrisearch.domain.category.entities.Subcategory
import be.ehb.gdt.nutrisearch.domain.category.exceptions.CategoryNotFoundException
import be.ehb.gdt.nutrisearch.domain.category.services.CategoryService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.util.ResourceUtils

@WebMvcTest(controllers = [CategoriesRestController::class])
@WithMockUser
class CategoriesRestControllerTest {
    private val id = "cat2"
    private val name = "Graanproducten"
    private val subcategories = mutableListOf(Subcategory("Cornflakes", "cat2sub1"), Subcategory("Brood", "cat2sub2"))
    private val category = Category(name, subcategories, id)

    @MockBean
    lateinit var categoryService: CategoryService

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `when GET all categories request is sent then status 200 is expected`() {
        val categories = listOf(
            Category(
                "Vleesproducten",
                mutableListOf(Subcategory("Vers Vlees", "cat1sub1")),
                "cat1"
            ),
            category
        )

        `when`(categoryService.getCategories()).thenReturn(categories)

        mockMvc.perform(get("/api/v1/categories").with(jwt()))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json(readJsonFromFile("categories/categories.json")))

        verify(categoryService).getCategories()
    }

    @Test
    fun `when GET request is sent then status 200 is expected`() {
        `when`(categoryService.getCategory("cat2")).thenReturn(category)

        mockMvc.perform(get("/api/v1/categories/{id}", id).with(jwt()))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json(readJsonFromFile("categories/category.json")))

        verify(categoryService).getCategory(id)
    }

    @Test
    fun `when GET request is sent and category does not exist then status 404 is expected`() {
        `when`(categoryService.getCategory(id)).thenThrow(CategoryNotFoundException::class.java)

        mockMvc.perform(get("/api/v1/categories/{id}", id).with(jwt()))
            .andDo(print())
            .andExpect(status().isNotFound())

        verify(categoryService).getCategory(id)
    }

    @Test
    fun `when POST request is sent then status 200 is expected`() {
        val json = "{ 'id': 'id', 'name': '$name' }"
        `when`(categoryService.createCategory(name)).thenReturn(Category(name, id = "id"))

        mockMvc.perform(post("/api/v1/categories").queryParam("name", name).with(jwt()))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(content().json(json))

        verify(categoryService).createCategory(name)
    }


    @Test
    fun `when POST request is invalid then status 400 is expected`() {
        mockMvc.perform(post("/api/v1/categories").with(jwt()))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect { assertEquals("Required parameter 'name' is not present.", it.response.errorMessage) }

        verifyNoInteractions(categoryService)
    }

    @Test
    fun `when category exists and valid PUT request is sent then status 204 expected`() {
        mockMvc.perform(put("/api/v1/categories/{id}", id).queryParam("name", name).with(jwt()))
            .andDo(print())
            .andExpect(status().isNoContent())

        verify(categoryService).updateCategory(id, name)
    }

    @Test
    fun `when category does not exists and PUT request is sent then status 404 is expected`() {
        `when`(categoryService.updateCategory(id, name)).thenThrow(CategoryNotFoundException::class.java)

        mockMvc.perform(put("/api/v1/categories/{id}", id).queryParam("name", name).with(jwt()))
            .andDo(print())
            .andExpect(status().isNotFound())

        verify(categoryService).updateCategory(id, name)
    }

    @Test
    fun `when category exists and invalid PUT request is sent then status 400 is expected`() {
        mockMvc.perform(put("/api/v1/categories/{id}", id).with(jwt()))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect { assertEquals("Required parameter 'name' is not present.", it.response.errorMessage) }

        verifyNoInteractions(categoryService)
    }

    @Test
    fun `when category does exists and DELETE request is sent then status 204 is expected`() {
        mockMvc.perform(delete("/api/v1/categories/{id}", id).with(jwt()))
            .andDo(print())
            .andExpect(status().isNoContent())

        verify(categoryService).deleteCategory(id)
    }

    @Test
    fun `when category does not exists and DELETE request is sent then status 404 is expected`() {
        `when`(categoryService.deleteCategory(id)).thenThrow(CategoryNotFoundException::class.java)

        mockMvc.perform(delete("/api/v1/categories/{id}", id).with(jwt()))
            .andDo(print())
            .andExpect(status().isNotFound())

        verify(categoryService).deleteCategory(id)
    }

    private fun readJsonFromFile(fileName: String): String {
        return String(ResourceUtils.getFile("classpath:$fileName").readBytes())
    }
}