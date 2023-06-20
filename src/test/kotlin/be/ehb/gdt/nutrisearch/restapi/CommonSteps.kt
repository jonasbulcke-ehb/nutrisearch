package be.ehb.gdt.nutrisearch.restapi

import be.ehb.gdt.nutrisearch.domain.userinfo.entities.UserInfo
import be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects.ActivityLevel
import be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects.Sex
import io.cucumber.java.Before
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.springframework.http.MediaType
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.core.oidc.StandardClaimNames
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDate

class CommonSteps : SpringIntegrationTest() {
    val role: GrantedAuthority
        get() = SimpleGrantedAuthority("ROLE_${testContext.role}")

    @Before
    fun setUp() {
        userInfoRepository.hardDeleteUserInfoByAuthId(testContext.authId ?: "user")
    }

    @Then("I expect status {int}")
    fun iExpectStatus(httpStatus: Int) {
        testContext.resultActions!!.andDo(print()).andExpect(status().`is`(httpStatus))
    }

    @When("I GET {string}")
    fun iGet(url: String) {
        testContext.resultActions = mockMvc.perform(
            get(url)
                .accept(MediaType.APPLICATION_JSON)
                .with(jwt().jwt { it.claim(StandardClaimNames.SUB, testContext.authId) }.authorities(role))
        )
    }

    @When("I PUT {string} with id {string} and content {string}")
    fun iPut(url: String, id: String, content: String) {
        var json = readJsonFromFile(content)
        json = json.replace("ID_TO_INSERT", id)
        testContext.resultActions = mockMvc.perform(
            put(url)
                .contentType(MediaType.APPLICATION_JSON).content(json)
                .with(jwt().jwt { it.claim(StandardClaimNames.SUB, testContext.authId) }.authorities(role))
        )
    }

    @When("I PUT {string}")
    fun iPut(url: String) {
        testContext.resultActions = mockMvc.perform(
            put(url)
                .with(jwt().jwt { it.claim(StandardClaimNames.SUB, testContext.authId) }.authorities(role))
        )
    }

    @When("I DELETE {string}")
    fun iDelete(url: String) {
        testContext.resultActions = mockMvc.perform(
            delete(url)
                .with(jwt().jwt { it.claim(StandardClaimNames.SUB, testContext.authId) }.authorities(role))
        )
    }

    @When("I POST {string} with content {string}")
    fun iPost(url: String, content: String) {
        val json = readJsonFromFile(content)
        testContext.resultActions = mockMvc.perform(
            post(url)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .with(jwt().jwt { it.claim(StandardClaimNames.SUB, testContext.authId) }.authorities(role))
        )
    }

    @When("I POST {string}")
    fun iPost(url: String) {
        testContext.resultActions = mockMvc.perform(
            post(url).with(jwt().jwt { it.claim(StandardClaimNames.SUB, testContext.authId) }.authorities(role))
        )
    }

    @When("I PATCH {string} with content {string}")
    fun iPatch(url: String, content: String) {
        val json = readJsonFromFile(content)
        testContext.resultActions = mockMvc.perform(
            patch(url)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .with(jwt().jwt { it.claim(StandardClaimNames.SUB, testContext.authId) }.authorities(role))
        )
    }

    @And("an userInfo with id {string} and authId {string}")
    fun anUserInfoWithIdAuthId(userInfoId: String, authId: String) {
        UserInfo(LocalDate.of(1999, 4, 29), ActivityLevel.Active, 178, Sex.Male, id = userInfoId).apply {
            this.authId = authId
        }.also {
            userInfoRepository.insertUserInfo(it)
        }
    }

    @Given("authenticated user with role {string}")
    fun authenticatedUserWithRole(role: String) {
        testContext.role = role
    }

    @And("authenticated user with authId {string}")
    fun authenticatedUserWithAuthId(authId: String) {
        testContext.authId = authId
    }

    @And("I expect the response body to contain {string}")
    fun iExpectTheResponseBodyToContain(filename: String) {
        val json = readJsonFromFile(filename)
        testContext.resultActions!!.andExpect(content().json(json))
    }

    @And("I expect the response body to contain boolean {string}")
    fun iExpectTheResponseBodyToContainBoolean(bool: String) {
        testContext.resultActions!!.andExpect(content().string(bool))
    }

    @And("I expect the response body to contain nothing")
    fun iExpectTheResponseBodyToContainNothing() {
        testContext.resultActions!!.andExpect(content().string(""))
    }

    @And("I expect the response body with content {string}")
    fun iExpectTheResponseBodyWithContent(json: String) {
        testContext.resultActions!!.andExpect(content().json(json))
    }
}