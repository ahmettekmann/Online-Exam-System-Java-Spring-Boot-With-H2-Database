package com.Ahmet.online_exam_system.controller;

import com.Ahmet.online_exam_system.model.Department;
import com.Ahmet.online_exam_system.model.Exam;
import com.Ahmet.online_exam_system.model.User;
import com.Ahmet.online_exam_system.repository.DepartmentRepository;
import com.Ahmet.online_exam_system.repository.UserRepository;
import com.Ahmet.online_exam_system.service.PasswordUtil;
import com.Ahmet.online_exam_system.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.sound.midi.spi.SoundbankReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    private final UserService userService;



    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;

    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("user", new User());

        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute("user") User user, HttpSession session, Model model) {
        String email = user.getEmail();
        User userdata = userRepository.findByEmail(email);
        session.setAttribute("userId", userdata.getId());
        if (userdata.getRole().equals("STUDENT") && PasswordUtil.verifyPassword(user.getPassword(),userdata.getPassword()) && userdata.getIsActive()) {
            Department department = userdata.getDepartment();
            Long departmentId = department.getId();
            session.setAttribute("departmentId", departmentId);
            return getRedirectURL(userdata.getRole());
        }

        if (userdata.getRole().equals("TEACHER") &&  PasswordUtil.verifyPassword(user.getPassword(),userdata.getPassword()) && userdata.getIsActive()) {
            return getRedirectURL(userdata.getRole());
        }

        if (userdata.getRole().equals("ADMIN") &&  PasswordUtil.verifyPassword(user.getPassword(),userdata.getPassword()) ) {
            return getRedirectURL(userdata.getRole());

        }



        else {
            model.addAttribute("error", "Hatalı email ya da şifre");
            return "login";
        }
    }

    private String getRedirectURL(String role) {
        switch (role) {
            case "TEACHER":
                return "redirect:teacher/profile";
            case "STUDENT":
                return "redirect:student/profile";
            case "ADMIN":
                return "redirect:admin/profile";
            default:
                return "redirect:error";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // Oturumu sonlandır
        }
        return "redirect:/login"; // Giriş sayfasına yönlendir
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user) {
        String password = PasswordUtil.hashPassword(user.getPassword());
        user.setIsActive(false);
        user.setPassword(password);
        userService.saveUser(user);
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("departments", departmentRepository.findAll());

        return "register";
    }


    @PostMapping("/admin/confirm-user/{userId}")
    public String confirmUser(@PathVariable("userId") Long userId, RedirectAttributes redirectAttributes) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            // Kullanıcı onaylandı olarak işaretle
            user.setIsActive(true);
            userRepository.save(user);
            redirectAttributes.addFlashAttribute("message", "Kullanıcı kayıt başvurusu onaylandı.");
        } else {
            redirectAttributes.addFlashAttribute("message", "Kullanıcı bulunamadı.");
        }
        return "redirect:/admin/confirm-user";
    }

    @GetMapping("/admin/confirm-user")
    public String confirmUserPage(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users",users);
        return "admin-confirm-user";
    }
    @GetMapping("/admin/departments")
    public String departmensPage(Model model) {
        List<Department> departments = departmentRepository.findAll();
        model.addAttribute("departments",departments);
        return "admin-departments";
    }

    @PostMapping("/admin/reject-user/{userId}")
    public String rejectUser(@PathVariable("userId") Long userId, RedirectAttributes redirectAttributes) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            // Kullanıcı onaylandı olarak işaretle
            userRepository.delete(user);
            redirectAttributes.addFlashAttribute("message", "Kullanıcı kayıt başvurusu silindi");
        } else {
            redirectAttributes.addFlashAttribute("message", "Kullanıcı bulunamadı.");
        }
        return "redirect:/admin/confirm-user";
    }

    @PostMapping("/admin/add/department")
    public String addDepartment(@RequestParam("departmentName") String departmentName, RedirectAttributes redirectAttributes) {

        Department department = new Department();
        department.setName(departmentName);
        departmentRepository.save(department);
        redirectAttributes.addFlashAttribute("successMessage", "Departman başarıyla eklendi.");
        return "redirect:/admin/departments";
    }
    @PostMapping("/admin/remove/department/{departmentId}")
    public String addDepartment(@PathVariable("departmentId") Long departmentId, RedirectAttributes redirectAttributes) {
        Department department = departmentRepository.findById(departmentId).orElse(null);
        departmentRepository.delete(department);

        return "redirect:/admin/departments";
    }

    @GetMapping("/admin/users")
    public String UsersPage(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users",users);
        return "admin-users";
    }

    @PostMapping("/admin/remove/user/{departmentId}")
    public String deleteUser(@PathVariable("userId") Long userId, RedirectAttributes redirectAttributes) {
        User user = userRepository.findById(userId).orElse(null);
        userRepository.delete(user);

        return "redirect:/admin/users";
    }





    @GetMapping("/allusers")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/student/profile")
    public String showStudentPanel(Model model, HttpSession session) {
        Long userId = (Long)session.getAttribute("userId");
        User userdata = userRepository.findById(userId).orElse(null);
        model.addAttribute("user",userdata);
        return "student";
    }
    @GetMapping("/admin/profile")
    public String showAdminPanel(Model model, HttpSession session) {
        Long userId = (Long)session.getAttribute("userId");
        User userdata = userRepository.findById(userId).orElse(null);
        model.addAttribute("user",userdata);
        return "admin-page";
    }


}
