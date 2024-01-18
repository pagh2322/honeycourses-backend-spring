package org.wooriverygood.api.course.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import org.wooriverygood.api.course.domain.Courses;
import org.wooriverygood.api.course.dto.CourseResponse;
import org.wooriverygood.api.course.dto.NewCourseRequest;
import org.wooriverygood.api.course.dto.NewCourseResponse;
import org.wooriverygood.api.course.repository.CourseRepository;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {
    @Mock
    private CourseRepository courseRepository;
    @InjectMocks
    private CourseService courseService;

    private List<Courses> testCourses = new ArrayList<>();

    private final int COURSES_COUNT = 10;

    @BeforeEach
    void setup() {
        for(int i = 0; i<COURSES_COUNT; i++) {
            testCourses.add(Courses.builder()
                    .course_name("Gaoshu" + i)
                    .course_category("Zhuanye")
                    .course_credit(5)
                    .isYouguan(0)
                    .kaikeYuanxi("Xinke")
                    .build());
        }
    }

    @Test
    @DisplayName("강의 목록 조회")
    void getCourses() {
        Mockito.when(courseRepository.findAll()).thenReturn(testCourses);

        List<CourseResponse> responses = courseService.findAll();

        // Then
        Assertions.assertThat(responses.size()).isEqualTo(COURSES_COUNT);

    }

    @Test
    @DisplayName("새로운 수업을 추가한다.")
    void addCourse() {
        NewCourseRequest newCourseRequest = NewCourseRequest.builder()
                .course_name("테스트 강의")
                .course_category("전공")
                .course_credit(5)
                .kaikeYuanxi("씬커")
                .isYouguan(0)
                .build();

        Mockito.when(courseRepository.save(any(Courses.class)))
                .thenReturn(Courses.builder()
                        .course_name(newCourseRequest.getCourse_name())
                        .course_category(newCourseRequest.getCourse_category())
                        .course_credit(newCourseRequest.getCourse_credit())
                        .kaikeYuanxi(newCourseRequest.getKaikeYuanxi())
                        .isYouguan(newCourseRequest.getIsYouguan())
                        .build());

        NewCourseResponse response = courseService.addCourse(newCourseRequest);

        Assertions.assertThat(response.getCourse_name()).isEqualTo(newCourseRequest.getCourse_name());
        Assertions.assertThat(response.getCourse_credit()).isEqualTo(newCourseRequest.getCourse_credit());
        Assertions.assertThat(response.getCourse_category()).isEqualTo(newCourseRequest.getCourse_category());
        Assertions.assertThat(response.getKaikeYuanxi()).isEqualTo(newCourseRequest.getKaikeYuanxi());
        Assertions.assertThat(response.getIsYouguan()).isEqualTo(newCourseRequest.getIsYouguan());
    }

}
