package be.ehb.gdt.nutrisearch.restapi.category

import be.ehb.gdt.nutrisearch.domain.category.entities.Category
import be.ehb.gdt.nutrisearch.domain.category.entities.Subcategory
import be.ehb.gdt.nutrisearch.domain.category.services.CategoryService
import be.ehb.gdt.nutrisearch.domain.exceptions.ResourceNotFoundException
import be.ehb.gdt.nutrisearch.restapi.auth.config.SecurityConfig
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.util.ResourceUtils

@WebMvcTest(value = [CategoriesRestController::class, SecurityConfig::class])
class CategoriesRestControllerTest {
    private val id = "cat2"
    private val name = "Graanproducten"
    private val subcategories = mutableListOf(Subcategory("Cornflakes", "cat2sub1"), Subcategory("Brood", "cat2sub2"))
    private val category = Category(name, subcategories, id)
    private val json = ResourceUtils.getFile("classpath:categories/category.json").readBytes()

    @MockBean
    lateinit var categoryService: CategoryService

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `when GET all categories request is sent then status 200 is expected`() {
        val json = ResourceUtils.getFile("classpath:categories/categories.json").readBytes();
        val categories = listOf(
            Category(
                "Vleesproducten",
                mutableListOf(Subcategory("Vers Vlees", "cat1sub1")),
                "cat1"
            ),
            category
        )

        whenever(categoryService.getCategories()).thenReturn(categories)

        mockMvc.perform(get("/api/v1/categories").with(jwt()))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json(String(json)))

        verify(categoryService).getCategories()
    }

    @Test
    fun `when GET request is sent then status 200 is expected`() {
        whenever(categoryService.getCategory("cat2")).thenReturn(category)

        mockMvc.perform(get("/api/v1/categories/{id}", id).with(jwt()))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json(String(json)))

        verify(categoryService).getCategory(id)
    }

    @Test
    fun `when GET request is sent and category does not exist then status 404 is expected`() {
        whenever(categoryService.getCategory(id)).thenThrow(ResourceNotFoundException::class.java)

        mockMvc.perform(get("/api/v1/categories/{id}", id).with(jwt()))
            .andDo(print())
            .andExpect(status().isNotFound)

        verify(categoryService).getCategory(id)
    }

    @Test
    fun `when POST request is sent then status 200 is expected`() {
        whenever(categoryService.createCategory(any())).thenReturn(category)

        mockMvc.perform(
            post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(jwt().authorities(SimpleGrantedAuthority("ROLE_dietitian")))
        )
            .andExpect(status().isCreated)
            .andExpect(content().json(String(json)))

        verify(categoryService).createCategory(category)
    }


    @Test
    fun `when POST request is invalid then status 400 is expected`() {
        whenever(categoryService.createCategory(argThat(CategoryMatcher()))).thenThrow(IllegalStateException())
        val json = """{"name": "Graanproducten", "subcategories": []}""";
        mockMvc.perform(
            post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(jwt().authorities(SimpleGrantedAuthority("ROLE_dietitian")))
        )
            .andDo(print())
            .andExpect(status().isBadRequest)

        verify(categoryService).createCategory(argThat(CategoryMatcher()))
    }

    @Test
    fun `when category exists and valid PUT request is sent then status 204 expected`() {
        mockMvc.perform(
            put("/api/v1/categories/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(jwt().authorities(SimpleGrantedAuthority("ROLE_dietitian")))
        )
            .andExpect(status().isNoContent)

        verify(categoryService).updateCategory(id, category)
    }

    @Test
    fun `when category does not exists and PUT request is sent then status 404 is expected`() {
        whenever(categoryService.updateCategory(id, category)).thenThrow(ResourceNotFoundException::class.java)

        mockMvc.perform(
            put("/api/v1/categories/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(jwt().authorities(SimpleGrantedAuthority("ROLE_dietitian")))
        )
            .andDo(print())
            .andExpect(status().isNotFound)

        verify(categoryService).updateCategory(id, category)
    }

    @Test
    fun `when category exists and invalid PUT request is sent then status 400 is expected`() {
        whenever(categoryService.updateCategory(any(), argThat(CategoryMatcher()))).thenThrow(IllegalStateException())
        val json = """{"name": "Graanproducten", "subcategories": [], "id": "$id"}""";
        mockMvc.perform(
            put("/api/v1/categories/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(jwt().authorities(SimpleGrantedAuthority("ROLE_dietitian")))
        )
            .andDo(print())
            .andExpect(status().isBadRequest)

        verify(categoryService).updateCategory(eq(id), argThat(CategoryMatcher()))
    }

    @Test
    fun `when ids does not match and PUT request is sent then status 400 is expected`() {
        whenever(categoryService.updateCategory(any(), argThat(CategoryMatcher()))).thenThrow(IllegalStateException())
        val json = """{"name": "Graanproducten", "subcategories": [], "id": "other"}""";
        mockMvc.perform(
            put("/api/v1/categories/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(jwt().authorities(SimpleGrantedAuthority("ROLE_dietitian")))
        )
            .andDo(print())
            .andExpect(status().isBadRequest)

        verify(categoryService).updateCategory(eq(id), argThat(CategoryMatcher()))
    }

    @Test
    fun `when category does exists and DELETE request is sent then status 204 is expected`() {
        mockMvc.perform(
            delete("/api/v1/categories/{id}", id)
                .with(jwt().authorities(SimpleGrantedAuthority("ROLE_dietitian")))
        )
            .andDo(print())
            .andExpect(status().isNoContent)

        verify(categoryService).deleteCategory(id)
    }

    @Test
    fun `when category does not exists and DELETE request is sent then status 404 is expected`() {
        whenever(categoryService.deleteCategory(id)).thenThrow(ResourceNotFoundException::class.java)

        mockMvc.perform(
            delete("/api/v1/categories/{id}", id)
                .with(jwt().authorities(SimpleGrantedAuthority("ROLE_dietitian")))
        )
            .andDo(print())
            .andExpect(status().isNotFound)

        verify(categoryService).deleteCategory(id)
    }
}