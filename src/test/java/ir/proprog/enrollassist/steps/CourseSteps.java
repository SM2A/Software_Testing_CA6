package ir.proprog.enrollassist.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import ir.proprog.enrollassist.Exception.ExceptionList;
import ir.proprog.enrollassist.domain.GraduateLevel;
import ir.proprog.enrollassist.domain.course.Course;
import ir.proprog.enrollassist.domain.student.Student;
import org.assertj.core.api.Assertions;

import java.util.List;

@CucumberContextConfiguration
public class CourseSteps {
    private Course newlyCreatedCourse;
    private ExceptionList exceptionList;

    @Given("sample courses")
    public void sampleCourses() {
    }

    @Given("a sample student")
    public void createSampleStudent() throws ExceptionList {
        Student student = new Student("810199999", "Undergraduate");
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
}
