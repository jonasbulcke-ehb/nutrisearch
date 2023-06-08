package be.ehb.gdt.nutrisearch.restapi.consumption

import org.junit.platform.suite.api.IncludeEngines
import org.junit.platform.suite.api.SelectClasspathResource
import org.junit.platform.suite.api.Suite

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features/consumptions")
class ConsumptionsRestControllerIT