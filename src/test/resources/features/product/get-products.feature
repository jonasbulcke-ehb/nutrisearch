Feature: get products

  Background:
    Given the following products
      | name               | categoryId              | isVerified | id        | ownerId        |
      | Cornflakes         | Graanproducten-id       | true       | product-1 | user-info-id-1 |
      | Mac & Cheese       | Bereide-gerechten-id    | false      | product-2 | user-info-id-2 |
      | Aardbeienconfituur | Beleg- en smeermiddelen | false      | product-3 | dietitian-1    |
      | Choco              | Beleg- en smeermiddelen | true       | product-4 | user-info-id-1 |
    And authenticated user with authId "user"

  Scenario: GET all products
    When I GET "/api/v1/products"
    Then I expect status 200
    And I expect the response body to contain "products/products.json"

  Scenario: GET one product
    When I GET "/api/v1/products/product-3"
    Then I expect status 200
    And I expect the response body to contain "products/confituur.json"
