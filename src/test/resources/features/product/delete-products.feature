Feature: delete a product

  Background:
    Given the following products
      | name  | categoryId            | isVerified | id        | ownerId        |
      | Choco | Beleg- en smeermiddel | false      | product-4 | user-info-id-1 |
    And authenticated user with authId "user"

  Scenario Outline: DELETE an existing product
    Given an userInfo with id "<userInfoId>" and authId "user"
    And authenticated user with role "<role>"
    When I DELETE "/api/v1/products/product-4"
    Then I expect status <responseStatus>
    Examples:
      | userInfoId     | role        | responseStatus |
      | user-info-id-1 | participant | 204            |
      | user-info-id-2 | participant | 403            |
      | user-info-id-3 | dietitian   | 204            |

    Scenario: DELETE a non-existing product
      Given an userInfo with id "user-info-id-4" and authId "user"
      When I DELETE "/api/v1/products/product-5"
      Then I expect status 404

