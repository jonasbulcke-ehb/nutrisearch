Feature: UserInfoRestController

  Scenario Outline: POST GET UPDATE DELETE userinfo
    Given an userInfo with id "<userInfoId>" and authId "user"
    And authenticated user with authId "<authId>"
    When I GET "/api/v1/userinfo/has-userinfo"
    Then I expect status 200
    And I expect the response body to contain boolean "<hasUserInfo>"
    When I PATCH "/api/v1/userinfo" with content "userinfo/updatable-userinfo.json"
    Then I expect status <patchResponse>
    When I POST "/api/v1/userinfo/weight" with content "userinfo/weight.json"
    Then I expect status <postWeightResponse>
    When I GET "/api/v1/userinfo"
    Then I expect status <getResponse>
    And I expect the response body to contain <json>
    When I POST "/api/v1/userinfo" with content "userinfo/userinfo.json"
    Then I expect status <postResponse>
    When I DELETE "/api/v1/userinfo"
    Then I expect status 204
    When I GET "/api/v1/userinfo/has-userinfo"
    Then I expect status 200
    And I expect the response body to contain boolean "false"
    Examples:
      | userInfoId               | authId | getResponse | hasUserInfo | postResponse | patchResponse | postWeightResponse | json                     |
      | 647487d2f7811144213c8ee5 | user   | 200         | true        | 400          | 200           | 204                | "userinfo/userinfo.json" |
      | 6482f910c65bb8a676f5cd7d | anon   | 403         | false       | 201          | 403           | 403                | nothing                  |


  Scenario: GET userinfo with studies
    Given an userInfo with id "6488b28b2f05ad42d76be792" and authId "participant"
    And authenticated user with authId "participant"
    Given the following studies
      | subject   | startDate  | endDate    | id      |
      | subject-1 | 2022-12-01 | 2023-04-04 | study-1 |
      | subject-2 | 2023-01-02 |            | study-2 |
    When I POST "/api/v1/studies/study-2/join"
    Then I expect status 204
    When I GET "/api/v1/userinfo"
    Then I expect status 200
    When I GET "/api/v1/studies"
    Then I expect status 200
