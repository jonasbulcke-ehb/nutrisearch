package be.ehb.gdt.nutrisearch.restapi.consumption

import be.ehb.gdt.nutrisearch.domain.consumption.entities.Consumption
import be.ehb.gdt.nutrisearch.domain.consumption.entities.Meal
import be.ehb.gdt.nutrisearch.domain.consumption.valueobjects.Product
import be.ehb.gdt.nutrisearch.domain.product.entities.Preparation
import be.ehb.gdt.nutrisearch.domain.product.valueobjects.ServingSize
import be.ehb.gdt.nutrisearch.domain.userinfo.entities.ActivityLevel
import be.ehb.gdt.nutrisearch.domain.userinfo.entities.Sex
import be.ehb.gdt.nutrisearch.domain.userinfo.entities.UserInfo
import be.ehb.gdt.nutrisearch.restapi.SpringIntegrationTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.util.ResourceUtils
import java.time.LocalDate

@WithMockUser
class ConsumptionsRestControllerIntegrationTest : SpringIntegrationTest() {
    private val consumptionId = "consumption-id"
    private val userInfoId = "my-user-info-id"

    private val consumption = Consumption(
        Meal.Breakfast,
        Product("Cornflakes", "prod-id-cornflakes"),
        ServingSize(),
        Preparation("Onbereid"),
        35.0,
        LocalDate.of(2023, 5, 30),
        consumptionId
    )

    private lateinit var json: String

    @BeforeEach
    fun setUp() {
        json = String(ResourceUtils.getFile("classpath:consumptions/consumption.json").readBytes())

        if (!userInfoRepository.existUserInfoById(userInfoId)) {
            userInfoRepository.insertUserInfo(
                UserInfo(LocalDate.now(), ActivityLevel.Active, 188, Sex.Male, id = userInfoId).apply {
                    authId = "user"
                })
        }
    }

    @Test
    fun `when POST consumption then expect status 200`() {
        mockMvc.perform(
            post("/api/v1/consumptions")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .with(jwt())
        ).andExpect(status().isOk())
    }

    @Test
    fun `when GET consumption then expect status 200`() {
        consumption.userInfoId = userInfoId
        consumptionRepository.saveConsumption(consumption)

        mockMvc.perform(
            get("/api/v1/consumptions/{id}", consumptionId).accept(MediaType.APPLICATION_JSON).with(jwt())
        )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json(json))
    }

    @Test
    fun `when GET consumption then expect status 403`() {
        consumption.apply {
            userInfoId = "other"
        }.also {
            consumptionRepository.saveConsumption(it)
        }

        mockMvc.perform(get("/api/v1/consumptions/{id}", consumptionId).accept(MediaType.APPLICATION_JSON).with(jwt()))
            .andDo(print())
            .andExpect(status().isForbidden())
    }
}