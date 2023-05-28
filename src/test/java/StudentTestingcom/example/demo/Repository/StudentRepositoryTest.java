package StudentTestingcom.example.demo.Repository;

import StudentTestingcom.example.demo.model.Gender;
import StudentTestingcom.example.demo.model.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository unitUnderTesting;

    @Test
    void itShouldCheckIfStudentEmailExists() {
        String email = "JimmyBob@gmail.com";
        // given
        Student student = new Student(
                "Jimmy",
                email,
                Gender.MALE
        );
        unitUnderTesting.save(student);

        // when
        Boolean expected = unitUnderTesting.selectExistsEmail(email);

        // then
        assertThat(expected).isTrue();
    }

    @Test
    void itShouldCheckIfStudentEmailDoesNotExists(){
        // given
        String email = "JimmyBob@gmail.com";

        // when
        Boolean expected = unitUnderTesting.selectExistsEmail(email);

        // then
        assertThat(expected).isFalse();
    }
}