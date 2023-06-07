Feature: Verify a product
  As a dietitian
  I want to verify a product

  Background:
    Given the following products
      | name               | categoryId            | isVerified | id        | ownerId        |
      | Aardbeienconfituur | Beleg- en smeermiddel | true       | product-1 | dietitian-1    |
      | Choco              | Beleg- en smeermiddel | false      | product-4 | user-info-id-1 |
    And authenticated user with authId "user"

  Scenario Outline: verify a product as a dietitian
    Given authenticated user with role "<role>"
    When I PUT "/api/v1/products/product-4/verify"
    Then I expect status <responseStatus>
    When I GET "/api/v1/products/product-4"
    Then I expect status 200
    And I expect the response body to contain a <isVerified> product
    Examples:
      | role        | responseStatus | isVerified |
      | dietitian   | 204            | verified   |
      | participant | 403            | unverified |
