package edu.hq.furniture_shop.Controller.admin;

import edu.hq.furniture_shop.Model.Order;
import edu.hq.furniture_shop.Model.OrderItem;
import edu.hq.furniture_shop.Repository.OrderItemRepository;
import edu.hq.furniture_shop.Repository.OrderRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class HoaDonController {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired private OrderItemRepository orderItemRepository;


    @GetMapping("/admin/bill/list")
    public String listBill(Model model, HttpSession seesion){
        String username = (String) seesion.getAttribute("username");

        if (username == null) {
            return "redirect:/home/login";
        }
        List<Order> order = orderRepository.findAll();
        model.addAttribute("orders", order);
        model.addAttribute("contentAdmin", "/admin/hoadon/list");
        return "/layouts/admin";

    }

    @GetMapping("/admin/bill/delete/{id}")
    public String deleteBill(@PathVariable Long id, RedirectAttributes redirectAttributes) {


        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isPresent()) {
            // Xóa hóa đơn
            orderRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("mess", "Bạn đã xóa thành công hóa đơn.");
        }
        return "redirect:/admin/bill/list";
    }


    // Trang chi tiết đơn hàng
    @GetMapping("/admin/bill/{id}")
    public String viewOrder(@PathVariable Long id, Model model) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            List<OrderItem> orderItems = orderItemRepository.findByOrder(order);
            model.addAttribute("order", order);
            model.addAttribute("orderItems", orderItems);
            model.addAttribute("contentAdmin", "/admin/hoadon/listDetails");
            return "/layouts/admin";
        } else {
            return "redirect:/admin/bill/list";
        }
    }
}
