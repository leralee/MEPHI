package ru.leralee.hibernate.web;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.leralee.hibernate.dto.response.AnalyticsSummary;
import ru.leralee.hibernate.service.AnalyticsService;

/**
 * @author valeriali
 * @project orm-project
 */
@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','REVIEWER')")
    @GetMapping("/summary")
    public AnalyticsSummary summary() {
        return analyticsService.getSummary();
    }
}
