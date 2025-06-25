package com.example.customs.controller;

import com.example.customs.dto.UserDTO;
import com.example.customs.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class UserWebController {

    private final UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "register";  // Thymeleaf шаблон register.html
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") UserDTO userDTO,
                               BindingResult result,
                               Model model) {
        if (result.hasErrors()) {
            return "register";  // если ошибки валидации — вернуться на форму
        }
        userService.register(userDTO);
        return "redirect:/success"; // страница после успешной регистрации
    }
//    @GetMapping("/success")
//    @ResponseBody
//    public String test() {
//        return "OK";
//    }

    @GetMapping("/success")
    public String showSuccessPage() {
        return "success";
    }

}
