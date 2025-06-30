package ru.job4j.cars.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cars.model.User;
import ru.job4j.cars.service.user.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    UserService userService;

    @PostMapping("/register")
    public String register(Model model, @ModelAttribute User wowUser) {
        var savedUser = userService.save(wowUser);
        if (savedUser.isEmpty()) {
            model.addAttribute("message", "The user with this login already exists.");
            return "errors/404";
        }
        return "redirect:/index";
    }

    @GetMapping("/register")
    public String getRegistrationPage() {
        return "users/register";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "users/login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute User wowUser, Model model, HttpServletRequest request) {
        System.out.println("Service proxy: " + userService.getClass());
        var userOptional = userService.findByLoginAndPassword(wowUser.getLogin(), wowUser.getPassword());
        if (userOptional.isEmpty()) {
            model.addAttribute("error", "This login or password is invalid");
            return "users/login";
        }
        HttpSession session = request.getSession();
        session.setAttribute("wowUser", userOptional.get());
        return "redirect:/index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/users/login";
    }
}
