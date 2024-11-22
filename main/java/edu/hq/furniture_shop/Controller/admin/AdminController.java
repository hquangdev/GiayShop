package edu.hq.furniture_shop.Controller.admin;

import edu.hq.furniture_shop.Model.Admin;
import edu.hq.furniture_shop.Model.Client;
import edu.hq.furniture_shop.Model.Order;
import edu.hq.furniture_shop.Repository.AdminRepository;
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

    @Autowired
    private AdminRepository adminRepository;

    //đăng kí
    @PostMapping("/admin/register")
    public String handleRegister(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String address,
            @RequestParam String phone,
            RedirectAttributes redirectAttributes) {

        // Kiểm tra email đã tồn tại chưa
        if (adminRepository.findByEmail(email).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Email đã được sử dụng!");
            return "redirect:/admin/register";
        }

        // Tạo một đối tượng User mới
        Admin admin = new Admin();

        admin.setName(name);
        admin.setEmail(email);
        admin.setPassword(password);
        admin.setAddress(address);
        admin.setPhone(phone);

        System.out.println("Saving admin: " + admin);
        // Lưu vào cơ sở dữ liệu
        adminRepository.save(admin);

        System.out.println("Saving admin: " + admin);

        redirectAttributes.addFlashAttribute("message", "Đăng ký thành công! Vui lòng đăng nhập.");
        return "redirect:/admin/register";
    }


    // Xử lý đăng nhập
    @PostMapping("/admin/login")
    public String handleLogin(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Optional<Admin> adminOptional = adminRepository.findByEmail(email);

        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();

            if (admin.getPassword().equals(password)) {

                session.setAttribute("loggedIn", true);
                session.setAttribute("username", admin.getName());
                session.setAttribute("email", email);
                return "redirect:/admin/home";
            } else {
                redirectAttributes.addFlashAttribute("error", "Mật khẩu không đúng!");
                return "redirect:/admin/login";
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Email không tồn tại!");
            return "redirect:/admin/login";
        }
    }


    // Đăng xuất
    @GetMapping("/admin/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/home";
    }

    //Khách hàng
    @GetMapping("/admin/khachhang")
    public String listKhachhang(HttpSession session, Model model) {

        String username = (String) session.getAttribute("username");

        if (username == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("khach", clientRepository.findAll());
        model.addAttribute("contentAdmin", "/admin/khachhang/list");
        return "/layouts/admin";
    }

    //Khách hàng
    @GetMapping("/admin/nhanvien")
    public String listNhanvien(HttpSession session, Model model) {

        String username = (String) session.getAttribute("username");

        if (username == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("nhanvien", adminRepository.findAll());
        model.addAttribute("contentAdmin", "/admin/nhanvien/list");
        return "/layouts/admin";
    }

    //xóa khách
    @GetMapping("/admin/khachhang/delete/{id}")
    public String deleteKhach(@PathVariable Long id, RedirectAttributes redirectAttributes) {


        Optional<Client> orderOptional = clientRepository.findById(id);
        if (orderOptional.isPresent()) {
            // Xóa hóa đơn
            clientRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("mess", "Bạn đã xóa thành công hóa đơn.");
        }
        return "redirect:/admin/bill/list";
    }

    //xóa nhân viên
    @GetMapping("/admin/nhanvien/delete/{id}")
    public String deleteNhanvien(@PathVariable Long id, RedirectAttributes redirectAttributes) {


        Optional<Admin> orderOptional = adminRepository.findById(id);
        if (orderOptional.isPresent()) {
            // Xóa hóa đơn
            adminRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("mess", "Bạn đã xóa thành công hóa đơn.");
        }
        return "redirect:/admin/bill/list";
    }

}
