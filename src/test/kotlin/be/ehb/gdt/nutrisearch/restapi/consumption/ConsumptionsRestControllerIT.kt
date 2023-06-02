package be.ehb.gdt.nutrisearch.restapi.consumption

import be.ehb.gdt.nutrisearch.domain.userinfo.entities.ActivityLevel
import be.ehb.gdt.nutrisearch.domain.userinfo.entities.Sex
import be.ehb.gdt.nutrisearch.domain.userinfo.entities.UserInfo
import be.ehb.gdt.nutrisearch.restapi.SpringIntegrationTest
import io.cucumber.java.Before
import io.cucumber.java.BeforeAll
import org.junit.platform.suite.api.IncludeEngines
import org.junit.platform.suite.api.SelectClasspathResource
import org.junit.platform.suite.api.Suite
import java.time.LocalDate

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features/consumption")
class ConsumptionsRestControllerIT : SpringIntegrationTest() {

}