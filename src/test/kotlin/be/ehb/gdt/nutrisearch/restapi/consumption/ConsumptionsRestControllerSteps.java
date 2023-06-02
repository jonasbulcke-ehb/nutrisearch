package be.ehb.gdt.nutrisearch.restapi.consumption;

import be.ehb.gdt.nutrisearch.restapi.SpringIntegrationTest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ConsumptionsRestControllerSteps extends SpringIntegrationTest {
	@Then("^I expect status (.*)$")
	public void iExpectStatus(int httpStatus) {
	}

	@Given("I have an user with existing authId")
	public void iHaveAnUserWithExistingAuthId() {
	}

	@Given("I have an user with non existing authId")
	public void iHaveAnUserWithNonExistingAuthId() {
	}

	@Given("I have an user with other authId")
	public void iHaveAnUserWithOtherAuthId() {
	}

	@When("I post a consumption")
	public void iPostAConsumption() {
	}

	@When("I get that consumption")
	public void iGetThatConsumption() {
	}

	@When("I update that consumption")
	public void iUpdateThatConsumption() {
	}

	@When("I delete that consumption")
	public void iDeleteThatConsumption() {
	}
}
