package be.ehb.gdt.nutrisearch.restapi.study

import be.ehb.gdt.nutrisearch.domain.study.entities.Study
import be.ehb.gdt.nutrisearch.restapi.SpringIntegrationTest
import io.cucumber.datatable.DataTable
import io.cucumber.java.en.Given
import java.time.LocalDate

class StudiesRestControllerSteps : SpringIntegrationTest() {
    @Given("the following studies")
    fun theFollowingStudies(table: DataTable) {
        table.asMaps().map { row ->
            Study(
                row["subject"]!!,
                LocalDate.parse(row["startDate"]!!),
                row["endData"]?.let { LocalDate.parse(it) },
                id = row["id"]!!
            )
        }.forEach(studyRepository::insertStudy)
    }
}