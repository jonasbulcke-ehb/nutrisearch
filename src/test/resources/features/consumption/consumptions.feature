Feature: ConsumptionsRestController

  Scenario Outline: POST GET UPDATE DELETE consumptions
    Given I have an user with <userInfoCondition>
    When I post a consumption
    Then I expect status <httpStatus>
    When I get that consumption
    Then I expect status <httpStatus>
    When I update that consumption
    Then I expect status <updatedHttpStatus>
    When I delete that consumption
    Then I expect status <httpStatus>
    Examples:
      | userInfoCondition    | httpStatus | updatedHttpStatus |
      | existing authId      | 200        | 204               |
      | non existing authId  | 403        | 403               |
      | other authId         | 200        | 204               |

