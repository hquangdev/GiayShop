package edu.hq.furniture_shop.Controller.admin;

import edu.hq.furniture_shop.Model.Client;
import edu.hq.furniture_shop.Repository.ClientRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class AdminController  {

    @Autowired
    private ClientRepository clientRepository;

    //đăng kí
    @PostMapping("/admin/register")
    public String handleRegister(@ModelAttribute Client client, RedirectAttributes redirectAttributes) {
        // Kiểm tra email đã tồn tại
        if (clientRepository.findByEmail(client.getEmail()).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Email đã được sử dụng!");
            return "redirect:/admin/user/add";
        }

        clientRepository.save(client);

        redirectAttributes.addFlashAttribute("message", "Đăng ký thành công! Vui lòng đăng nhập.");
        return "redirect:/admin/user/add";
    }

    @GetMapping("/admin/user/add")
    public String register(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/home/login";
        }

        model.addAttribute("contentAdmin", "admin/user/register");
        return "layouts/admin";
    }

    // Đăng xuất
    @GetMapping("/admin/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/home";
    }

    @GetMapping("/admin/user")
    public String listUser(HttpSession session, Model model) {

        int role = (int) session.getAttribute("role");
        String username = (String) session.getAttribute("username");

        if (username == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("user", clientRepository.findAll());
        model.addAttribute("role", role);
        model.addAttribute("username", username);
        model.addAttribute("contentAdmin", "/admin/user/list");
        return "/layouts/admin";
    }



    //xóa
    @GetMapping("/admin/user/delete/{id}")
    public String deleteKhach(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        Optional<Client> orderOptional = clientRepository.findById(id);
        if (orderOptional.isPresent()) {

            clientRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("mess", "Bạn đã xóa thành công hóa đơn.");
        }
        return "redirect:/admin/user";
    }


}
