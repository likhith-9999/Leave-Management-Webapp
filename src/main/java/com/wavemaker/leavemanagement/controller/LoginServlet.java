package com.wavemaker.leavemanagement.controller;

import com.wavemaker.leavemanagement.service.EmployeeService;
import com.wavemaker.leavemanagement.service.UserService;
import com.wavemaker.leavemanagement.service.impl.EmployeeServiceImpl;
import com.wavemaker.leavemanagement.service.impl.UserServiceImpl;
import com.wavemaker.leavemanagement.util.CookieHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;


@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginServlet.class);
    //    public static final Map<String, List<String>> cookieMap = new ConcurrentHashMap<>();
    private final UserService userService = new UserServiceImpl();
    private final EmployeeService employeeService = new EmployeeServiceImpl();


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String emailID = req.getParameter("emailID");
        String password = req.getParameter("password");

        try {
            boolean login = userService.authenticate(emailID, password);

            if (login) {
                String unqId = UUID.randomUUID().toString();
                Cookie cookie = new Cookie("unqId", unqId);
                String manager = String.valueOf(employeeService.checkManagerService(emailID));
                CookieHolder.addKey(unqId, Arrays.asList(emailID, manager));


                cookie.setPath("/");
                cookie.setSecure(req.isSecure());
                resp.addCookie(cookie);

                LOGGER.info("User {} logged in successfully", emailID);
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(String.format("{\"status\": \"ok\", \"message\": \"Logged in\", \"isManager\":" + " %b}", Boolean.parseBoolean(manager)));
            } else {
                LOGGER.warn("Login attempt failed for user {}", emailID);
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.getWriter().write("{\"status\": \"error\", \"message\": \"Wrong credentials\"}");
            }
        } catch (Exception e) {
            LOGGER.error("Error during login for user {}", emailID, e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"status\": \"error\", \"message\": \"Error in login\"}");
        }
    }
}
