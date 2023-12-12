package com.samukelo.SkeroChat.Controller;

import com.samukelo.SkeroChat.Model.User;
import com.samukelo.SkeroChat.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor //to inject our dependencies
public class UserController {
    private final UserService userService;

    @GetMapping("/register")
    public String showRegisterPage(Model model){
        model.addAttribute("registerRequest", new User());
        return "register";
    }
    @GetMapping("/signin")
    public String showLoginPage(Model model){
        model.addAttribute("loginRequest", new User());
        return "login";
    }
    @PostMapping("/register")
    public String register(@ModelAttribute User user){
        System.out.println("register request"+user);
        User registeredUser = userService.registerUser(user.getFirstname(), user.getLastname(), user.getEmail(), user.getPassword());
        return registeredUser == null ? "redirect:/error_page" : "redirect:/login";
    }
    @PostMapping("/login")
    public String login(@ModelAttribute User user, Model model){
        System.out.println("login request"+user);
        User authenticated = userService.authenticate(user.getEmail(), user.getPassword());

        if(authenticated != null){
            model.addAttribute("userLogin", authenticated.getEmail());
            return "redirect:/index";
        }else{
            return "redirect:/error_page";
        }
    }
    @MessageMapping("/user.addUser")
    @SendTo("/user/topic")
    public User addUser(@Payload User user){
        userService.saveUser(user);

        return user;
    }

    @MessageMapping("/user.disconnectUser")
    @SendTo("/user/topic")
    public User disconnect(@Payload User user){
        userService.disconnected(user);

        return user;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> findConnectedUser(){
        return ResponseEntity.ok(userService.findConnectedUsers());
    }
}
