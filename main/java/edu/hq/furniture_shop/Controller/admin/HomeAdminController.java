package edu.hq.furniture_shop.Controller.admin;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeAdminController {

    @GetMapping("/admin/home")
    public String showAdminHomePage(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");

        if (username == null) {
            return "redirect:/admin/login";
        }
        model.addAttribute("username", username);
        model.addAttribute("contentAdmin", "/admin/home-admin");
        return"layouts/admin";
    }

    @GetMapping("/admin/login")
    public String login(){
        return"/admin/login";
    }

    @GetMapping("/admin/register")
    public String register(Model model){
        return"/admin/register";
    }

}
