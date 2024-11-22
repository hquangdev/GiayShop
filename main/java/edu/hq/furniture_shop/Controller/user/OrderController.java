package edu.hq.furniture_shop.Controller.user;

import edu.hq.furniture_shop.Model.Client;
import edu.hq.furniture_shop.Model.Order;
import edu.hq.furniture_shop.Model.OrderItem;
import edu.hq.furniture_shop.Model.Product;
import edu.hq.furniture_shop.Repository.OrderItemRepository;
import edu.hq.furniture_shop.Repository.OrderRepository;
import edu.hq.furniture_shop.Repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
@Controller

public class OrderController {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;


    @PostMapping("/home/order")
    public String processOrder(@RequestParam("name") String name,
                               @RequestParam("email") String email,
                               @RequestParam("phone") String phone,
                               @RequestParam("address") String address,
                               @RequestParam("note") String note,
                               @RequestParam("tongia") Double tongia, HttpSession httpSession) {

        // Tạo đối tượng Order mới và thiết lập thông tin từ form
        Order order = new Order();
        order.setName(name);
        order.setEmail(email);
        order.setPhone(phone);
        order.setAddress(address);
        order.setNote(note);
        order.setTongia(tongia);  // Sử dụng tổng giá trị từ form

        // Lưu đơn hàng vào cơ sở dữ liệu
        Order savedOrder = orderRepository.save(order);

        // Lấy giỏ hàng từ session
        Map<Long, Integer> cart = (Map<Long, Integer>) httpSession.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            throw new IllegalArgumentException("Giỏ hàng rỗng hoặc không tồn tại.");
        }

        // Duyệt qua giỏ hàng và tạo các OrderItem
        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            Long productId = entry.getKey();
            Integer quantity = entry.getValue();

            // Lấy thông tin sản phẩm từ cơ sở dữ liệu
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Sản phẩm không tồn tại"));

            // Tạo OrderItem từ giỏ hàng
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);
            orderItem.setOrder(savedOrder);

            // Lưu OrderItem vào cơ sở dữ liệu
            orderItemRepository.save(orderItem);
        }

        // Xóa giỏ hàng khỏi session sau khi đặt hàng
        httpSession.removeAttribute("cart");

        return "redirect:/home/thank-you";
    }

    //
    @GetMapping("/home/thank-you")
    public String thankYou(Model model, HttpSession seesion){
        model.addAttribute("content", "/client/thank-you");
        return "layouts/client";
    }

}
