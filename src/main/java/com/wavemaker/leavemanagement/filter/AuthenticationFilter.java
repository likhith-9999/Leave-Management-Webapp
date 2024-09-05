package com.wavemaker.leavemanagement.filter;


import com.wavemaker.leavemanagement.exception.InvalidDateException;
import com.wavemaker.leavemanagement.util.CookieHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebFilter(urlPatterns = {"/myLeaves", "/home.html", "/myTeamLeaves", "/employee", "/leaveType"})
public class AuthenticationFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException {
        LOGGER.info("Entering AuthenticationFilter");

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        Cookie[] cookies = req.getCookies();
        String emailId = null;
        String isManager = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("unqId".equals(cookie.getName())) {
                    List<String> employeeDetailsList = CookieHolder.getEmployeeDetails(cookie.getValue());
                    emailId = employeeDetailsList.get(0);
                    isManager = employeeDetailsList.get(1);
                }
            }
        }

        try {
            if ((emailId == null || isManager == null)) {
                LOGGER.info("User not authenticated, redirecting to login page");

                throw new InvalidDateException("User not authenticated");
            }
            req.setAttribute("emailId", emailId);
            req.setAttribute("isManager", isManager);
            filterChain.doFilter(req, res);
        } catch (Exception e) {
            LOGGER.error("Authentication error: {}", e.getMessage());
            res.sendRedirect(req.getContextPath() + "/index.html");
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}