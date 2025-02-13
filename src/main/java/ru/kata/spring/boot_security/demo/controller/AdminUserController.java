package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.services.security.AccountDetails;

import java.util.Arrays;

@Controller
public class AdminUserController {
    private UserService userservice;

    @Autowired
    public AdminUserController(UserService userservice) {
        this.userservice = userservice;
    }

    @GetMapping("/user")
    public String showUser(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AccountDetails accountDetails = (AccountDetails) authentication.getPrincipal();
        model.addAttribute("user", accountDetails.getUser());
        return "user";
    }

    @GetMapping("/")
    public String home() {
        if(userservice.listUsers().isEmpty())
            userservice.addUser(new User("admin","admin", Arrays.asList("ROLE_ADMIN")));
        return "index";
    }

    @GetMapping("/admin")
    public String showUsers(Model model) {

        model.addAttribute("users", userservice.listUsers());
        return "admin";
    }


    @GetMapping("/admin/register")
    public String register(Model model) {

        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/admin/register")
    public String inputUser(@ModelAttribute("user") User user) {

        userservice.addUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/{id}/edit")
    public String editUser(@PathVariable("id") Long id, Model model) {

        model.addAttribute("editable_user", userservice.getUser(id));
        return "edit";
    }

    @PatchMapping("/admin/{id}")
    public String edit(@ModelAttribute("editable_user") User user, @PathVariable("id") Long id) {

        userservice.editUser(id, user);
        return "redirect:/admin";
    }

    @DeleteMapping("/admin/{id}")
    public String delete(@PathVariable("id") Long id) {

        userservice.deleteUser(id);
        return "redirect:/admin";
    }

    @RequestMapping("/forbidden")
    public String accessDenied() {
        return "forbidden";
    }
}




