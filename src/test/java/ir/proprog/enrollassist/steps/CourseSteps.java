package ir.proprog.enrollassist.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import ir.proprog.enrollassist.Exception.ExceptionList;
import ir.proprog.enrollassist.domain.course.Course;
import ir.proprog.enrollassist.domain.student.Student;
import org.assertj.core.api.Assertions;

@CucumberContextConfiguration
public class CourseSteps {
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
            Course course = new Course(courseNumber, title, credits, graduateLevel);
        } catch (ExceptionList e) {
            exceptionList = e;
        }
    }

    @Then("the course is created correctly")
    public void checkTheCourseIsCreatedCorrectly() {
        Assertions.assertThat(exceptionList).isNull();
    }
}
