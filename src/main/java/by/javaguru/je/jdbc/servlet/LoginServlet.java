package by.javaguru.je.jdbc.servlet;

import by.javaguru.je.jdbc.dto.UserDto;
import by.javaguru.je.jdbc.service.UserService;
import by.javaguru.je.jdbc.utils.JspHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

import static by.javaguru.je.jdbc.utils.UrlPath.LOGIN;

@Log4j2
@WebServlet(LOGIN)
public class LoginServlet extends HttpServlet {

    private final UserService userService = UserService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(JspHelper.getPath("login")).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        userService.login(req.getParameter("email"), req.getParameter("password"))
                .ifPresentOrElse(userDto -> onLoginSuccess(userDto, req, resp),
                        () -> onLoginFail(req, resp));
    }

    @SneakyThrows
    private void onLoginFail(HttpServletRequest req, HttpServletResponse resp) {
        log.error("Incorrect email or password");
        resp.sendRedirect("/login?error&email=" + req.getParameter("email"));
    }

    @SneakyThrows
    private void onLoginSuccess(UserDto userDto, HttpServletRequest req, HttpServletResponse resp) {
        req.getSession().setAttribute("user", userDto);
        resp.sendRedirect("/flights");
    }
}
