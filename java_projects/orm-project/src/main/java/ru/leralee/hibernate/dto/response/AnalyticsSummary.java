package ru.leralee.hibernate.dto.response;

import lombok.Builder;
import lombok.Value;

/**
 * @author valeriali
 * @project orm-project
 */
@Value
@Builder
public class AnalyticsSummary {

    long totalCourses;
    long totalStudents;
    long totalTeachers;
    long totalAssignments;
    long totalQuizSubmissions;
    double averageCourseRating;
}
