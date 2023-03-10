package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dao.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.dao.service.UserServiceImpl;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Controller
//@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("admin")
public class UserController {

    private final UserServiceImpl userService;
    private final RoleServiceImpl roleService;

    @Autowired
    public UserController(UserServiceImpl userService, RoleServiceImpl roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String users(Model model) {
        model.addAttribute("users", userService.listUsers());
        return "userss";
    }

    @GetMapping("create")
    public String createUserForm(User user, Model model) {
        model.addAttribute("roleList", roleService.listRoles());
        return "create";
    }

    @PostMapping("create")
    public String createUser(/*@ModelAttribute("user") */User user) {
        List<String> lsr = user.getRoles().stream().map(r -> r.getRole()).collect(Collectors.toList());
        List<Role> liRo = roleService.listByRole(lsr);
        user.setRoles(liRo);
        userService.add(user);
        return "redirect:/admin";
    }

    @GetMapping("delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }

    @GetMapping("update/{id}")
    public String updateUserForm(@PathVariable("id") Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("roleList", roleService.listRoles());
        return "update";
    }

    @PostMapping("update")
    public String updateUser(@ModelAttribute User user) {
        userService.update(user);
        return "redirect:/admin";
    }
}
