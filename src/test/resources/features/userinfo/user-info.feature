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
    Then I expect status <patchResponse>
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
      | userInfoId | authId | getResponse | hasUserInfo | postResponse | patchResponse | json                     |
      | userinfo-1 | user   | 200         | true        | 400          | 204           | "userinfo/userinfo.json" |
      | userinfo-2 | anon   | 404         | false       | 201          | 404           | nothing                  |



