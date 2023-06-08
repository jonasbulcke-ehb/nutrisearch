Feature: put a product

  Background:
    Given the following products
      | name               | categoryId        | isVerified | id        | ownerId        |
      | Cornflakes         | Graanproducten-id | true       | product-1 | user-info-id-1 |
      | Abrikozenconfituur | Zoet beleg        | false      | product-3 | user-info-id-1 |
    And authenticated user with authId "user"

  Scenario Outline: PUT an own product
    Given authenticated user with role "<role>"
    And an userInfo with id "<userInfoId>" and authId "user"
    When I PUT "/api/v1/products/product-3" with id "product-3" and content "products/confituur.json"
    Then I expect status <status>
    When I GET "/api/v1/products/product-3"
    Then I expect status 200
    And I expect the response body with content "<content>"
    And I expect the response body to contain a <isVerified> product
    Examples:
      | role        | userInfoId     | status | content                                                                 | isVerified |
      | participant | user-info-id-1 | 204    | {'name': 'Aardbeienconfituur', 'categoryId': 'Beleg- en smeermiddelen'} | unverified |
      | participant | user-info-id-2 | 403    | {'name': 'Abrikozenconfituur', 'categoryId': 'Zoet beleg'}              | unverified |
      | dietitian   | user-info-id-1 | 204    | {'name': 'Aardbeienconfituur', 'categoryId': 'Beleg- en smeermiddelen'} | verified   |