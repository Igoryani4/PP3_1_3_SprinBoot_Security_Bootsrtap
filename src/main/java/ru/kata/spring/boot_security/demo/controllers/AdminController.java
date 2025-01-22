package ru.kata.spring.boot_security.demo.controllers;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import ru.kata.spring.boot_security.demo.service.CustomUserDetailService;

import java.util.List;

@Controller()
public class AdminController {

    private final CustomUserDetailService customUserDetailService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;


    public AdminController(CustomUserDetailService customUserDetailService, UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.customUserDetailService = customUserDetailService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        List<User> users = customUserDetailService.findAll();
        model.addAttribute("users", users);
        return "admin";
    }

    @GetMapping("/register")
    public String createUserForm(User user, Model model) {
        model.addAttribute("user", user);
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("roles", roles);
        return "register";
    }

    @PostMapping("/register")
    public String createUser(User user,Role role) {
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        customUserDetailService.saveUser(user, role);
        return "redirect:/admin";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id){
        customUserDetailService.delete(id);
        return "redirect:/admin";
    }

    @GetMapping("/update/{id}")
    public String updateUserForm(@PathVariable("id") Long id, Model model){
        User user = customUserDetailService.findById(id);
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        return "update";
    }

    @PostMapping("/update")
    public String updateUser(User user){
        customUserDetailService.update(user);
        return "redirect:/admin";
    }
}
