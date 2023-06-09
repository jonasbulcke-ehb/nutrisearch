package be.ehb.gdt.nutrisearch.restapi.category

import be.ehb.gdt.nutrisearch.domain.category.entities.Subcategory
import be.ehb.gdt.nutrisearch.domain.category.services.SubcategoryService
import be.ehb.gdt.nutrisearch.domain.exceptions.ResourceNotFoundException
import be.ehb.gdt.nutrisearch.restapi.auth.config.SecurityConfig
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.util.ResourceUtils

@WebMvcTest(value = [SubcategoriesRestController::class, SecurityConfig::class])
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

        mockMvc.perform(get("/api/v1/categories/{id}/subcategories", parentId).with(jwt()))
            .andExpect(status().isOk)
            .andExpect(content().json(readSubcategoryJsonFromJson()))

        verify(subcategoryService).getSubcategories(parentId)
    }

    @Test
    fun `when category does not exist and GET request is sent then status 404 is expected`() {
        `when`(subcategoryService.getSubcategories(parentId)).thenThrow(ResourceNotFoundException::class.java)

        mockMvc.perform(get("/api/v1/categories/{id}/subcategories", parentId).with(jwt()))
            .andExpect(status().isNotFound)

        verify(subcategoryService).getSubcategories(parentId)
    }

    @Test
    fun `when category exists and has subcategories and GET request is sent then status 200 is expected`() {
        val json = "{'id': $id, 'name': 'Brood'}"

        `when`(subcategoryService.getSubcategory(parentId, id)).thenReturn(subcategory)

        mockMvc.perform(get("/api/v1/categories/{id}/subcategories/{subId}", parentId, id).with(jwt()))
            .andExpect(status().isOk)
            .andExpect(content().json(json))

        verify(subcategoryService).getSubcategory(parentId, id)
    }

    @Test
    fun `when category exists and subcategory does not exists and GET request is sent then status 404 is expected`() {
        `when`(subcategoryService.getSubcategory(parentId, id)).thenThrow(ResourceNotFoundException::class.java)

        mockMvc.perform(get("/api/v1/categories/{id}/subcategories/{subId}", parentId, id).with(jwt()))
            .andExpect(status().isNotFound)

        verify(subcategoryService).getSubcategory(parentId, id)
    }

    @Test
    fun `when category exists and POST request is sent then status 201 is expected`() {
        mockMvc.perform(
            post("/api/v1/categories/{id}/subcategories", parentId).queryParam("name", name)
                .with(jwt().authorities(SimpleGrantedAuthority("ROLE_dietitian")))
        )
            .andExpect(status().isCreated)

        verify(subcategoryService).createSubcategory(parentId, name)
    }

    @Test
    fun `when category does not exists and POST request is sent then status 404 is expected`() {
        `when`(subcategoryService.createSubcategory(parentId, name))
            .thenThrow(ResourceNotFoundException::class.java)

        mockMvc.perform(
            post("/api/v1/categories/{id}/subcategories", parentId).queryParam("name", name)
                .with(jwt().authorities(SimpleGrantedAuthority("ROLE_dietitian")))
        )
            .andExpect(status().isNotFound)

        verify(subcategoryService).createSubcategory(parentId, name)
    }

    @Test
    fun `when category and subcategory exists and PUT request is sent then status 204 is expected`() {
        mockMvc.perform(
            put("/api/v1/categories/{id}/subcategories/{subId}", parentId, id).queryParam("name", name)
                .with(jwt().authorities(SimpleGrantedAuthority("ROLE_dietitian")))
        )
            .andExpect(status().isNoContent)

        verify(subcategoryService).updateSubcategory(parentId, id, name)
    }

    @Test
    fun `when category or subcategory does not exists and PUT request is sent then status 404 is expected`() {
        `when`(subcategoryService.updateSubcategory(parentId, id, name)).thenThrow(
            ResourceNotFoundException::class.java
        )

        mockMvc.perform(
            put("/api/v1/categories/{id}/subcategories/{subId}", parentId, id)
                .queryParam("name", name)
                .with(jwt().authorities(SimpleGrantedAuthority("ROLE_dietitian")))
        )
            .andExpect(status().isNotFound)

        verify(subcategoryService).updateSubcategory(parentId, id, name)
    }

    @Test
    fun `when category and subcategory exists and DELETE request is sent then status 204 is expected`() {
        mockMvc.perform(
            delete("/api/v1/categories/{id}/subcategories/{subId}", parentId, id)
                .with(jwt().authorities(SimpleGrantedAuthority("ROLE_dietitian")))
        )
            .andExpect(status().isNoContent)

        verify(subcategoryService).deleteSubcategory(parentId, id)
    }

    @Test
    fun `when category and subcategory exists and forbidden DELETE request is sent then status 403 is expected`() {
        mockMvc.perform(delete("/api/v1/categories/{id}/subcategories/{subId}", parentId, id).with(jwt()))
            .andDo(print())
            .andExpect(status().isForbidden)

        verifyNoInteractions(subcategoryService)
    }

    @Test
    fun `when category and subcategory exists and unauthorized DELETE request is sent then status 401 is expected`() {
        mockMvc.perform(delete("/api/v1/categories/{id}/subcategories/{subId}", parentId, id).with(anonymous()))
            .andDo(print())
            .andExpect(status().isUnauthorized)

        verifyNoInteractions(subcategoryService)
    }


    @Test
    fun `when category or subcategory does not exist and DELETE request is sent then status 404 is expected`() {
        `when`(subcategoryService.deleteSubcategory(parentId, id)).thenThrow(ResourceNotFoundException::class.java)

        mockMvc.perform(
            delete("/api/v1/categories/{id}/subcategories/{subId}", parentId, id)
                .with(jwt().authorities(SimpleGrantedAuthority("ROLE_dietitian")))
        )
            .andExpect(status().isNotFound)

        verify(subcategoryService).deleteSubcategory(parentId, id)
    }

    private fun readSubcategoryJsonFromJson() =
        String(ResourceUtils.getFile("classpath:categories/subcategories.json").readBytes())
}