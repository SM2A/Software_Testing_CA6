Feature: Course

  Background:
    Given sample courses
    Given a sample student

  Scenario Outline: user creates new course correctly.(there is no exception)
    When user wants to create new course with courseNumber=<courseNumber>, title=<title>, credits=<credits> for <graduateLevel> students
    Then the course is created correctly
    Examples:
      | courseNumber | title                 | credits | graduateLevel   |
      | "1111111"    | "Math1"               | 3       | "Undergraduate" |
      | "2222222"    | "Advanced Algorithms" | 3       | "Masters"       |
      | "3333333"    | "Optimization"        | 3       | "PHD"           |

