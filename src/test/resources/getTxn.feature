Feature: Get Txn Req API test

  Scenario: Get Txn Req API Test
    Given I have tenant as "gorilla"
    When I call the auth endpoint with username: "mifos" and password: "password"
    Then I should get a valid token
    When I call the get txn summary API with expected status of 200
    Then I should get non empty response
    And  My reposnse should contain

