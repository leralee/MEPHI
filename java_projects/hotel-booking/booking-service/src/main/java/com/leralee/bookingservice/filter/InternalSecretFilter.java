package com.leralee.bookingservice.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author valeriali
 * @project hotel-booking
 */
@Component
public class InternalSecretFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest http = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String internalHeader = http.getHeader("X-Internal-Secret");
        String path = http.getRequestURI();

        if (path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs")) {
            chain.doFilter(request, response);
            return;
        }

        if (!"gateway-approved".equals(internalHeader)) {
            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            res.getWriter().write("Access denied: requests must go through API Gateway");
            return;
        }

        chain.doFilter(request, response);
    }
}
