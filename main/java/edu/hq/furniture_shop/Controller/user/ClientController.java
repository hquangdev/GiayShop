package edu.hq.furniture_shop.Controller.user;

import edu.hq.furniture_shop.Model.Client;
import edu.hq.furniture_shop.Repository.ClientRepository;
import edu.hq.furniture_shop.Service.ClientService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ClientController {

    @Autowired
    private ClientService userService;

    // Xử lý Đăng nhập
    @PostMapping("/home/login")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password, Model model,
                        HttpSession session, RedirectAttributes redirectAttributes) {
        // Xác thực thông tin người dùng từ service
        Client user = userService.authenticate(email, password);

        if (user != null) {
            // Lưu thông tin người dùng vào session
            session.setAttribute("loggedInUser", user);
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", user.getRole());

            int role = user.getRole();

            if (role == 1 || role == 2) {
                // Admin và Nhân viên sẽ chuyển tới trang quản lý
                redirectAttributes.addFlashAttribute("mess", "Bạn đã đăng nhập thành công.");
                
                return "redirect:/admin/home";
            } else if (role == 3) {
                // Khách sẽ chuyển tới trang home
                redirectAttributes.addFlashAttribute("mess", "Bạn đã đăng nhập thành công.");
                return "redirect:/home";
            }

        } else {
            // Nếu thông tin đăng nhập sai
            redirectAttributes.addFlashAttribute("mess", "Tài khoản hoặc mật khẩu sai");
            return "redirect:/home/login";
        }

        // Nếu role không khớp, trả về trang login (trường hợp không hợp lệ)
        redirectAttributes.addFlashAttribute("mess", "Role không hợp lệ.");
        return "redirect:/home/login";
    }



    // Xử lý Đăng xuất
    @GetMapping("/home/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/home";
    }

    // Xử lý Đăng ký
    @PostMapping("/home/register")
    public String register(@RequestParam("email") String email,
                           @RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam("phone") String phone,
                           Model model, RedirectAttributes redirectAttributes) {
        // Kiểm tra xem email đã tồn tại chưa
        if (userService.checkEmailExists(email)) {
            redirectAttributes.addFlashAttribute("mess", "Email đã tồn tại");
            model.addAttribute("content", "/client/login");
            return "/layouts/client";
        }

        int role = 2;

        // Tạo tài khoản mới
        Client newUser = new Client();
        newUser.setEmail(email);
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setPhone(phone);
        newUser.setRole(role);

        userService.saveClient(newUser);
        redirectAttributes.addFlashAttribute("mess", "Bạn đã đăng kí thành công");
        model.addAttribute("content", "/client/login");
        return "/layouts/client";
    }
}
