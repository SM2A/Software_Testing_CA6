Feature: Course

  Scenario Outline: user creates new course correctly.(there is no exception)
    When user wants to create new course with courseNumber=<courseNumber>, title=<title>, credits=<credits> for <graduateLevel> students
    Then the course is created correctly with properties <courseNumber>, <title>, <credits> and <graduateLevel>
    Examples:
      | courseNumber | title                          | credits | graduateLevel   |
      | "1111111"    | "Math1"                        | 3       | "Undergraduate" |
      | "2222222"    | "Advanced Algorithms"          | 3       | "Masters"       |
      | "3333333"    | "Optimization"                 | 3       | "PHD"           |
      | "4444444"    | "life style and communication" | 0       | "Undergraduate" |

  Scenario Outline: user can't create new course if he/she provides invalid properties
    When user wants to create new course with courseNumber=<courseNumber>, title=<title>, credits=<credits> for <graduateLevel> students
    Then user gets <exception>
    Examples:
      | courseNumber | title   | credits | graduateLevel   | exception                                                    |
      | "1111111"    | ""      | 3       | "Undergraduate" | "Course must have a name."                                   |
      | ""           | "Math1" | 3       | "Undergraduate" | "Course number cannot be empty."                             |
      | "111111a"    | "Math1" | 3       | "Undergraduate" | "Course number must be number."                              |
      | "11111"      | "Math1" | 3       | "Undergraduate" | "Course number must contain 7 numbers."                      |
      | "1111111"    | "Math1" | 5       | "Undergraduate" | "Credit must be one of the following values: 0, 1, 2, 3, 4." |
      | "1111111"    | "Math1" | 3       | "undergraduate" | "Graduate level is not valid."                               |
      | "1111111"    | "Math1" | 3       | "undefined"     | "Graduate level is not valid."                               |

  Scenario: The course can be taken by the student if he/she has passed its prerequisites
    Given course with courseNumber="1111111", title="MATH1", credits=3 for "Undergraduate" students
    * course with courseNumber="2222222", title="PHYS1", credits=3 for "Undergraduate" students
    * course with courseNumber="1111111", title="PHYS2", credits=3 for "Undergraduate" students with "MATH1,PHYS1" prerequisites
    Given a sample student
    * the student has passed the "MATH1,PHYS1" with grades "10,12" in terms "11112,11112"
    When student wants to take the "PHYS2"
    Then student can take the course without any enrollment rule violations

    Scenario: The course can't be taken by the student if he/she hasn't passed its prerequisites
      Given course with courseNumber="1111111", title="MATH1", credits=3 for "Undergraduate" students
      * course with courseNumber="2222222", title="PHYS1", credits=3 for "Undergraduate" students
      * course with courseNumber="1111111", title="PHYS2", credits=3 for "Undergraduate" students with "MATH1,PHYS1" prerequisites
      Given a sample student
      * the student has passed the "MATH1" with grades "10" in terms "11112"
      When student wants to take the "PHYS2"
      Then student gets enrollment rule violations and can't take the course