package com.ood.tamanage.controller;

import com.ood.tamanage.pojo.*;
import com.ood.tamanage.service.AdminService;
import com.ood.tamanage.service.LecturerService;
import com.ood.tamanage.service.StudentService;
import com.ood.tamanage.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin  // 允许跨域
public class LoginController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private LecturerService lecturerService;
    @Autowired
    private AdminService adminService;

    @GetMapping({"/login", "/"})
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate(); // 销毁会话
        return "login"; // 重定向到登录页面
    }

    @ResponseBody
    @PostMapping("/loginValidate")
    public Result loginValidate(@RequestBody User user, HttpSession session) {
        if (user == null || user.getUsername() == null || user.getPassword() == null) {
            return Result.error("the username or password could not be null");
        }

        Result result = validateWithService(adminService, user);
        if (result != null && result.isSuccess()) {
            session.setAttribute("user", result.getData());
            return result;
        }

        result = validateWithService(lecturerService, user);
        if (result != null && result.isSuccess()) {
            session.setAttribute("user", result.getData());
            return result;
        }

        result = validateWithService(studentService, user);
        if (result != null && result.isSuccess()) {
            session.setAttribute("user", result.getData());
            return result;
        }

        return Result.error("the username or password is wrong");
    }

    private Result validateWithService(UserService service, User user) {
        User validatedUser = service.validate(user);
        if (validatedUser != null) {
            String redirectUrl = determineRedirectUrl(validatedUser); // 根据用户类型决定重定向 URL
            return Result.success(validatedUser, redirectUrl);
        }
        return null;
    }


    private String determineRedirectUrl(User user) {
        // 根据用户类型来决定重定向的 URL
        if (user instanceof Admin) {
            return "admins/index_admin";
        } else if (user instanceof Lecturer) {
            return "lecturers/index_lecturer";
        } else if (user instanceof Student) {
            return "students/index_student";
        }
        return "/login"; // 默认重定向
    }

}
