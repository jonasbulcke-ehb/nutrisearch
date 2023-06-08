package be.ehb.gdt.nutrisearch.restapi.consumption

import be.ehb.gdt.nutrisearch.domain.consumption.entities.Consumption
import be.ehb.gdt.nutrisearch.domain.consumption.entities.Meal
import be.ehb.gdt.nutrisearch.domain.consumption.valueobjects.Product
import be.ehb.gdt.nutrisearch.domain.product.entities.Preparation
import be.ehb.gdt.nutrisearch.domain.product.valueobjects.ServingSize
import be.ehb.gdt.nutrisearch.restapi.SpringIntegrationTest
import io.cucumber.java.DataTableType
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import java.time.LocalDate

class ConsumptionsRestControllerSteps : SpringIntegrationTest() {

    @DataTableType
    fun consumptionsTransformer(row: Map<String, String?>): Consumption {
        val product = Product("Cornflakes", "my-cornflakes-id")
        val preparation = Preparation("Onbereid")
        val timestamp = LocalDate.parse(row["timestamp"])
        return Consumption(
            Meal.Breakfast,
            product,
            ServingSize(),
            preparation,
            35.0,
            timestamp,
            row["consumptionId"]!!
        ).apply {
            userInfoId = row["userInfoId"]!!
        }
    }

    @Given("the following consumptions")
    fun theFollowingConsumptions(consumptions: List<Consumption>) {
        consumptions.forEach(consumptionRepository::saveConsumption)
    }


    @And("I expect the response contains the list of the following ids")
    fun theResponseContainsTheFollowingIds(ids: List<String>) {
        val json = ids.map { "{\"id\":\"$it\"}" }.toString()
        testContext.resultActions!!.andDo(print()).andExpect(content().json(json))
    }

}
