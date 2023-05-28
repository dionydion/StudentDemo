package StudentTestingcom.example.demo.Service;

import StudentTestingcom.example.demo.Exception.BadRequestException;
import StudentTestingcom.example.demo.Exception.StudentNotFoundException;
import StudentTestingcom.example.demo.Repository.StudentRepository;
import StudentTestingcom.example.demo.model.Gender;
import StudentTestingcom.example.demo.model.Student;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Autowired
    @InjectMocks
    private StudentService unitUnderTesting;

    @Mock
    private StudentRepository studentRepository;

    @Test
    void getAllStudents() {
        unitUnderTesting.getAllStudents();

        verify(studentRepository).findAll();
    }

    @Test
    void canAddStudent() {
        // given
        Student student = new Student(
                "Jimmy",
                "JimmyBob@gmail.com",
                Gender.MALE
        );
        // when
        unitUnderTesting.addStudent(student);

        // then
        ArgumentCaptor<Student> argumentCaptor =
                ArgumentCaptor.forClass(Student.class);

        verify(studentRepository).save(argumentCaptor.capture());

        Student capturedStudent = argumentCaptor.getValue();

        assertThat(capturedStudent).isEqualTo(student);
    }

    @Test
    void willThrowWillEmailIsTaken() {
        // given
        Student student = new Student(
                "Jimmy",
                "JimmyBob@gmail.com",
                Gender.MALE
        );

        given(studentRepository.selectExistsEmail(student.getEmail()))
                .willReturn(true);
        // when
        // then

        assertThatThrownBy(() -> unitUnderTesting.addStudent(student))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Email " + student.getEmail() + " taken");
    }

    @Test
    void canDeleteStudent() {
        // given
        Long studentId = 1L;

        given(studentRepository.existsById(studentId))
                .willReturn(true);


        // when
        unitUnderTesting.deleteStudent(studentId);

        // then
        ArgumentCaptor<Long> argumentCaptor =
                ArgumentCaptor.forClass(Long.class);

        verify(studentRepository).deleteById(argumentCaptor.capture());

        Long capturedStudentId = argumentCaptor.getValue();

        assertThat(capturedStudentId).isEqualTo(studentId);
    }

    @Test
    void willThrowIfStudentDoesNotExists() {
        // given
        Long studentId = 1L;

        given(studentRepository.existsById(studentId))
                .willReturn(false);
        // when
        // then

        assertThatThrownBy(() -> unitUnderTesting.deleteStudent(studentId))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("Student with id " + studentId + " does not exists");

    }
}