Feature: post product

  Scenario Outline: POST a product
    Given authenticated user with role "<role>"
    And authenticated user with authId "user"
    And an userInfo with id "user-info-id" and authId "user"
    When I POST "/api/v1/products" with content "products/request-cornflakes.json"
    Then I expect status 201
    And I expect the response body to contain <responseBody>
    Examples:
      | role        | responseBody                        |
      | dietitian   | a verified product                  |
      | participant | "products/response-cornflakes.json" |
