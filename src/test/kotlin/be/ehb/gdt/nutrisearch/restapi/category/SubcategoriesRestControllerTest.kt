package be.ehb.gdt.nutrisearch.restapi.category

import be.ehb.gdt.nutrisearch.domain.category.entities.Subcategory
import be.ehb.gdt.nutrisearch.domain.category.exceptions.CategoryNotFoundException
import be.ehb.gdt.nutrisearch.domain.category.services.SubcategoryService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.util.ResourceUtils

@WebMvcTest(controllers = [SubcategoriesRestController::class])
class SubcategoriesRestControllerTest {
    private val parentId = "cat2"
    private val id = "cat2sub2"
    private val name = "Brood"
    private val subcategory = Subcategory(name, id)
    private val subcategories = mutableListOf(Subcategory("Cornflakes", "cat2sub1"), subcategory)

    @MockBean
    private lateinit var subcategoryService: SubcategoryService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `when category exists and has subcategories and GET request is sent then expect status 200`() {
        `when`(subcategoryService.getSubcategories(parentId)).thenReturn(subcategories)

        mockMvc.perform(get("/api/v1/categories/{id}/subcategories", parentId))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(readSubcategoryJsonFromJson()))

        verify(subcategoryService).getSubcategories(parentId)
    }

    @Test
    fun `when category does not exist and GET request is sent then status 404 is expected`() {
        `when`(subcategoryService.getSubcategories(parentId)).thenThrow(CategoryNotFoundException::class.java)

        mockMvc.perform(get("/api/v1/categories/{id}/subcategories", parentId))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound())

        verify(subcategoryService).getSubcategories(parentId)
    }

    @Test
    fun `when category exists and has subcategories and GET request is sent then status 200 is expected`() {
        val json = "{'id': $id, 'name': 'Brood'}"

        `when`(subcategoryService.getSubcategory(parentId, id)).thenReturn(subcategory)

        mockMvc.perform(get("/api/v1/categories/{id}/subcategories/{subId}", parentId, id))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(json))

        verify(subcategoryService).getSubcategory(parentId, id)
    }

    @Test
    fun `when category exists and subcategory does not exists and GET request is sent then status 404 is expected`() {
        `when`(subcategoryService.getSubcategory(parentId, id)).thenThrow(CategoryNotFoundException::class.java)

        mockMvc.perform(get("/api/v1/categories/{id}/subcategories/{subId}", parentId, id))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound())

        verify(subcategoryService).getSubcategory(parentId, id)
    }

    @Test
    fun `when category exists and POST request is sent then status 204 is expected`() {
        mockMvc.perform(post("/api/v1/categories/{id}/subcategories", parentId).queryParam("name", name))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNoContent())

        verify(subcategoryService).createSubcategory(parentId, name)
    }

    @Test
    fun `when category does not exists and POST request is sent then status 404 is expected`() {
        `when`(subcategoryService.createSubcategory(parentId, name))
            .thenThrow(CategoryNotFoundException::class.java)

        mockMvc.perform(post("/api/v1/categories/{id}/subcategories", parentId).queryParam("name", name))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound())

        verify(subcategoryService).createSubcategory(parentId, name)
    }

    @Test
    fun `when category and subcategory exists and PUT request is sent then status 204 is expected`() {
        mockMvc.perform(put("/api/v1/categories/{id}/subcategories/{subId}", parentId, id).queryParam("name", name))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNoContent())

        verify(subcategoryService).updateSubcategory(parentId, id, name)
    }

    @Test
    fun `when category or subcategory does not exists and PUT request is sent then status 404 is expected`() {
        `when`(subcategoryService.updateSubcategory(parentId, id, name)).thenThrow(
            CategoryNotFoundException::class.java
        )

        mockMvc.perform(
            put("/api/v1/categories/{id}/subcategories/{subId}", parentId, id).queryParam(
                "name", name
            )
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound())

        verify(subcategoryService).updateSubcategory(parentId, id, name)
    }

    @Test
    fun `when category and subcategory exists and DELETE request is sent then status 204 is expected`() {
        mockMvc.perform(delete("/api/v1/categories/{id}/subcategories/{subId}", parentId, id))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNoContent())

        verify(subcategoryService).deleteSubcategory(parentId, id)
    }

    @Test
    fun `when category or subcategory does not exist and DELETE request is sent then status 404 is expected`() {
        `when`(subcategoryService.deleteSubcategory(parentId, id)).thenThrow(CategoryNotFoundException::class.java)

        mockMvc.perform(delete("/api/v1/categories/{id}/subcategories/{subId}", parentId, id))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound())

        verify(subcategoryService).deleteSubcategory(parentId, id)
    }

    private fun readSubcategoryJsonFromJson() =
        String(ResourceUtils.getFile("classpath:categories/subcategories.json").readBytes())
}