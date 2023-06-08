package be.ehb.gdt.nutrisearch.restapi

import io.cucumber.spring.ScenarioScope
import org.springframework.stereotype.Component
import org.springframework.test.web.servlet.ResultActions

@ScenarioScope
@Component
class TestContext {
    var resultActions: ResultActions? = null
    var authId: String? = "user"
    var role: String = "participant"
}