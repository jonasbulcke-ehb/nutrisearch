package be.ehb.gdt.nutrisearch.restapi.category

import be.ehb.gdt.nutrisearch.restapi.SpringIntegrationTest
import org.junit.platform.suite.api.IncludeEngines
import org.junit.platform.suite.api.SelectClasspathResource
import org.junit.platform.suite.api.Suite

@IncludeEngines("cucumber")
@SelectClasspathResource("features/category")
class CategoriesRestControllerIT : SpringIntegrationTest()