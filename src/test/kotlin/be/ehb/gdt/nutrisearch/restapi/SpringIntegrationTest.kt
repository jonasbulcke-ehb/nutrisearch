package be.ehb.gdt.nutrisearch.restapi

import be.ehb.gdt.nutrisearch.domain.consumption.repositories.ConsumptionRepository
import be.ehb.gdt.nutrisearch.domain.userinfo.repositories.UserInfoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.util.ResourceUtils

@EnableAutoConfiguration
@SpringBootTest
@AutoConfigureDataMongo
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ComponentScan("be.ehb.gdt.nutrisearch")
class SpringIntegrationTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var userInfoRepository: UserInfoRepository

    @Autowired
    lateinit var consumptionRepository: ConsumptionRepository

    fun readJsonFromFile(fileName: String) = String(ResourceUtils.getFile("classpath:$fileName").readBytes())

}

