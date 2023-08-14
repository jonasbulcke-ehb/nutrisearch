package be.ehb.gdt.nutrisearch.domain.study.repositories

import be.ehb.gdt.nutrisearch.domain.consumption.entities.Consumption
import be.ehb.gdt.nutrisearch.domain.consumption.valueobjects.Meal
import be.ehb.gdt.nutrisearch.domain.consumption.valueobjects.Product
import be.ehb.gdt.nutrisearch.domain.product.entities.Preparation
import be.ehb.gdt.nutrisearch.domain.product.valueobjects.Nutrient
import be.ehb.gdt.nutrisearch.domain.product.valueobjects.ServingSize
import be.ehb.gdt.nutrisearch.domain.study.entities.Study
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Ignore
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.util.ResourceUtils
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
@Ignore
class StudyMongoRepositoryTest {
    @Mock
    private lateinit var mongoTemplate: MongoTemplate
    private lateinit var repo: StudyRepository

    @BeforeEach
    fun setUp() {
        repo = StudyMongoRepository(mongoTemplate)
    }

    @Nested
    @Ignore
    inner class FindConsumptions {
        private val timestamp = LocalDate.parse("2023-06-16")
        private lateinit var consumptions: List<Consumption>

        @BeforeEach
        fun setUp() {
            val studies = listOf<Study>(
//                study,
//                Study(
//                    "Sub-2",
//                    LocalDate.of(2023, 1, 12),
//                    LocalDate.of(2024, 1, 12),
//                    listOf("other-participant-1", "other-participant-2"),
//                    id = "study-2"
//                ),
            )

            val json = ResourceUtils.getFile("classpath:consumptions/consumptions.json")
            val mapper = ObjectMapper()
            consumptions = mapper.readTree(json).map { node ->
                val nutrients =
                    Nutrient.values()
                        .associateWith { nutrient -> node["preparation"]["nutrients"][nutrient.toString()].let { it?.asDouble() } }
                Consumption(
                    Meal.valueOf(node["meal"].asText()),
                    node["product"].let { Product(it["name"].asText(), it["_id"]["\$oid"].asText()) },
                    node["servingSize"].let { ServingSize(it["grams"].asInt(), it["name"].asText()) },
                    node["preparation"].let {
                        Preparation(
                            it["name"].asText(),
                            nutrients,
                            it["_id"]["\$oid"].asText()
                        )
                    },
                    node["amount"].asDouble(),
                    LocalDate.parse(node["timestamp"]["\$date"].asText().split("T")[0]),
                    node["_id"].asText()
                )
            }
            val filteredConsumptions = consumptions.filter { it.timestamp == timestamp }


//            whenever(mongoTemplate.findOne(Query(Criteria.where("_id").`is`(STUDY_ID)), Study::class.java)).thenReturn(
//                study
//            )
            val query = Query(
                Criteria.where("timestamp").`is`(timestamp).and("userInfoId").`in`(
                    participantIds
                )
            )
//            whenever(mongoTemplate.find(query, Consumption::class.java)).thenReturn(filteredConsumptions)

        }

        @Test
        @Ignore
        fun test_retrieveConsumptions() {
//            val retrievedConsumptions = repo.findConsumptions(STUDY_ID, timestamp)

            val filteredConsumptions = consumptions.filter { it.timestamp == timestamp }

//            assertEquals(filteredConsumptions, retrievedConsumptions)

//            inOrder(mongoTemplate).apply {
//                verify(mongoTemplate).findOne(Query(Criteria.where("_id").`is`(STUDY_ID)), Study::class.java)
//                verify(mongoTemplate).find(any(), eq(Consumption::class.java))
//            }
        }
    }

    companion object {
        private const val STUDY_ID = "study-id-1"
        private val participantIds = listOf(
            "participant-1",
            "participant-2",
            "participant-3"
        )

//        private val study = Study("Sub-1", LocalDate.of(2021, 9, 15), null, participantIds, id = STUDY_ID)
    }
}