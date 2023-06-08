Feature: ConsumptionsRestController

  Scenario: CRUD consumptions with authenticated userinfo
    Given the following consumptions
      | timestamp  | consumptionId | userInfoId                 |
      | 2023-05-23 | consumption-1 | user-info-id-of-auth-user  |
      | 2023-05-23 | consumption-2 | user-info-id-of-auth-user  |
      | 2023-05-23 | consumption-3 | user-info-id-of-other-user |
      | 2023-05-23 | consumption-4 | user-info-id-of-auth-user  |
      | 2023-05-22 | consumption-5 | user-info-id-of-auth-user  |
      | 2023-05-22 | consumption-6 | user-info-id-of-other-user |
      | 2023-05-22 | consumption-7 | user-info-id-of-auth-user  |
    And an userInfo with id "user-info-id-of-auth-user" and authId "user"
    And authenticated user with authId "user"
    When I POST "/api/v1/consumptions" with content "consumptions/consumption.json"
    Then I expect status 201
    When I GET "/api/v1/consumptions/consumption-1"
    Then I expect status 200
    When I GET "/api/v1/consumptions/consumption-3"
    Then I expect status 403
    When I GET "/api/v1/consumptions?timestamp=2023-05-23"
    And I expect status 200
    And I expect the response contains the list of the following ids
      | consumption-1 |
      | consumption-2 |
      | consumption-4 |
    When I PUT "/api/v1/consumptions/consumption-1" with id "consumption-1" and content "consumptions/consumption-with-id.json"
    Then I expect status 204
    When I PUT "/api/v1/consumptions/consumption-3" with id "consumption-3" and content "consumptions/consumption-with-id.json"
    Then I expect status 403
    When I DELETE "/api/v1/consumptions/consumption-1"
    Then I expect status 204
    When I DELETE "/api/v1/consumptions/consumption-1"
    Then I expect status 403

  Scenario: CRUD consumptions without userinfo
    Given the following consumptions
      | timestamp  | consumptionId | userInfoId                 |
      | 2023-05-23 | consumption-1 | user-info-id-of-auth-user  |
      | 2023-05-23 | consumption-3 | user-info-id-of-other-user |
    And authenticated user with authId "anon"
    When I POST "/api/v1/consumptions" with content "consumptions/consumption.json"
    Then I expect status 404
    When I GET "/api/v1/consumptions/consumption-1"
    Then I expect status 404
    When I GET "/api/v1/consumptions?timestamp=2023-05-23"
    And I expect status 404
    When I PUT "/api/v1/consumptions/consumption-1" with id "consumption-1" and content "consumptions/consumption-with-id.json"
    Then I expect status 404
    When I DELETE "/api/v1/consumptions/consumption-1"
    Then I expect status 404
