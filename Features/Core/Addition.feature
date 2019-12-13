Feature: Addition

  #Test01 -- -1
  Scenario Outline: Test01
    And Go to URL "<rowindex>"
    And Enter First Name using "name" from excel "<rowindex>"
    And Enter Message using "message" from excel "<rowindex>"
    And Enter Sum using "sum" from excel "<rowindex>"
    And Check numbers have changed

    Examples:
      |rowindex|
      |       1|

  #Test02 -- Addition
  Scenario Outline: Test02
    And Go to URL "<rowindex>"
    And Enter First Name using "name" from excel "<rowindex>"
    And Enter Message using "message" from excel "<rowindex>"
    And Get Sum of Both numbers
    And Click on Submit Button
    And Check message

    Examples:
      |rowindex|
      |       1|

