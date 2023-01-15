package ir.proprog.enrollassist.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import ir.proprog.enrollassist.Exception.ExceptionList;
import ir.proprog.enrollassist.domain.EnrollmentRules.EnrollmentRuleViolation;
import ir.proprog.enrollassist.domain.GraduateLevel;
import ir.proprog.enrollassist.domain.course.Course;
import ir.proprog.enrollassist.domain.student.Student;
import org.assertj.core.api.Assertions;

import java.util.*;
import java.util.stream.Collectors;

@CucumberContextConfiguration
public class CourseSteps {
    private Student student;
    private Map<String, Course> courses = new HashMap<>();
    private Course newlyCreatedCourse;
    private ExceptionList exceptionList;
    private List<EnrollmentRuleViolation> violations;

    @Given("a sample student")
    public void createSampleStudent() throws ExceptionList {
        student = new Student("810199999", "Undergraduate");
    }

    @When("user wants to create new course with courseNumber={string}, title={string}, credits={int} for {string} students")
    public void createNewCourse(String courseNumber, String title, int credits, String graduateLevel) {
        try {
            newlyCreatedCourse = new Course(courseNumber, title, credits, graduateLevel);
        } catch (ExceptionList e) {
            exceptionList = e;
        }
    }

    @Then("the course is created correctly with properties {string}, {string}, {int} and {string}")
    public void checkTheCourseIsCreatedCorrectly(String courseNumber, String title, int credits, String graduateLevel) {
        Assertions.assertThat(exceptionList).isNull();
        Assertions.assertThat(newlyCreatedCourse).isNotNull();
        Assertions.assertThat(newlyCreatedCourse.getCourseNumber().getCourseNumber()).isEqualTo(courseNumber);
        Assertions.assertThat(newlyCreatedCourse.getTitle()).isEqualTo(title);
        Assertions.assertThat(newlyCreatedCourse.getCredits()).isEqualTo(credits);
        Assertions.assertThat(newlyCreatedCourse.getGraduateLevel()).isEqualTo(GraduateLevel.valueOf(graduateLevel));
    }

    @Then("user gets {string}")
    public void userGetsException(String exceptionMessage) {
        Assertions.assertThat(exceptionList).isNotNull();
        List<String> exceptions = exceptionList.getExceptions().stream().map(Throwable::getMessage).toList();
        Assertions.assertThat(exceptions).containsExactlyInAnyOrder(exceptionMessage);
    }

    @Given("course with courseNumber={string}, title={string}, credits={int} for {string} students")
    public void addCourse(String courseNumber, String title, int credits, String graduateLevel) throws ExceptionList {
        courses.put(title,  new Course(courseNumber, title, credits, graduateLevel));
    }

    @Given("course with courseNumber={string}, title={string}, credits={int} for {string} students with {string} prerequisites")
    public void addCourseWithPrerequisites(String courseNumber, String title, int credits, String graduateLevel, String prereq) throws ExceptionList {
        Set<Course> prerequisites = Arrays.stream(prereq.split(",")).map(n->courses.get(n))
                .collect(Collectors.toSet());
        Course course = new Course(courseNumber, title, credits, graduateLevel);
        course.setPrerequisites(prerequisites);
        courses.put(title, course);
    }

    @Given("the student has passed the {string} with grades {string} in terms {string}")
    public void studentHasPassedTheCoursesWithGradesInTerm(String courseNames, String grades, String terms) throws ExceptionList {
        List<Course> passedCourses = Arrays.stream(courseNames.split(",")).map(n->courses.get(n)).toList();
        List<Double> courseGrades = Arrays.stream(grades.split(",")).mapToDouble(Double::parseDouble).boxed().toList();
        List<String> passedTerms = Arrays.stream(terms.split(",")).toList();

        for (int i = 0; i < passedCourses.size(); i++)
            student.setGrade(passedTerms.get(i), passedCourses.get(i), courseGrades.get(i));

    }

    @When("student wants to take the {string}")
    public void studentWantsToTakeTheCourse(String courseName) {
        violations = courses.get(courseName).canBeTakenBy(student);
    }

    @Then("student can take the course without any enrollment rule violations")
    public void studentCanTakeTheCourseWithoutAnyEnrollmentRuleViolations() {
        Assertions.assertThat(violations).isEmpty();
    }

    @Then("student gets enrollment rule violations and can't take the course")
    public void studentGetsEnrollmentRuleViolationsAndCanNotTakeTheCourse() {
        Assertions.assertThat(violations).isNotEmpty();
    }
}
