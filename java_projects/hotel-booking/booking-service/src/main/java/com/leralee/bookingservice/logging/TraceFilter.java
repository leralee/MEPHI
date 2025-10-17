package com.leralee.bookingservice.logging;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

/**
 * @author valeriali
 * @project hotel-booking
 */
@Component
public class TraceFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        try {
            HttpServletRequest http = (HttpServletRequest) request;
            String traceId = http.getHeader("X-Trace-Id");
            if (traceId == null || traceId.isEmpty()) {
                traceId = UUID.randomUUID().toString();
            }
            MDC.put("traceId", traceId);
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
