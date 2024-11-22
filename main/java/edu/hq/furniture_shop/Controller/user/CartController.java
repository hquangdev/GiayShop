package edu.hq.furniture_shop.Controller.user;

import edu.hq.furniture_shop.Model.Client;
import edu.hq.furniture_shop.Model.Product;
import edu.hq.furniture_shop.Repository.ProductRepository;
import edu.hq.furniture_shop.Service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/home")
public class CartController extends BaseController {

    @Autowired
    private ProductRepository productRepository;


    // Thêm sản phẩm vào giỏ hàng
    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam("productId") Long productId, HttpSession session) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Sản phẩm không tồn tại"));

        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
        }

        if (cart.containsKey(productId)) {
            cart.put(productId, cart.get(productId) + 1);
        } else {
            cart.put(productId, 1);
        }

        // Lưu giỏ hàng vào session
        session.setAttribute("cart", cart);

        return "redirect:/home/cart";
    }


    @GetMapping("/cart")
    public String showCart(Model model, HttpSession session) {

        Client loggedInUser = (Client) session.getAttribute("loggedInUser");

        if (loggedInUser != null) {
            model.addAttribute("fullName", loggedInUser.getUsername());
            model.addAttribute("email", loggedInUser.getEmail());
        }

        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
        }

        // Lấy danh sách sản phẩm từ repository dựa trên productId trong giỏ hàng
        List<Product> cartProducts = cart.entrySet().stream()
                .map(entry -> {
                    Product product = productRepository.findById(entry.getKey())
                            .orElseThrow(() -> new IllegalArgumentException("Sản phẩm không tồn tại"));
                    product.setQuantity(entry.getValue());
                    return product;
                })
                .collect(Collectors.toList());

        // Tính tổng giá trị đơn hàng
        int totalPrice = cartProducts.stream().mapToInt(product -> product.getPrice() * product.getQuantity()).sum();

        // Thêm giỏ hàng và tổng giá trị vào model để hiển thị trong view
        model.addAttribute("cart", cartProducts);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("content", "/Client/cart");
        return "layouts/client";  // Layout giỏ hàng
    }

    @PostMapping("/update-cart")
    public String updateCart(@RequestParam("productId") Long productId,
                             @RequestParam("quantity") int quantity,
                             @RequestParam("action") String action, HttpSession session,
                             RedirectAttributes redirectAttributes) {
        // Lấy giỏ hàng từ session
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
        }

        // Kiểm tra và xử lý hành động tăng/giảm số lượng
        if (cart.containsKey(productId)) {
            int currentQuantity = cart.get(productId);

            if ("increase".equals(action)) {
                currentQuantity += 1;
            } else if ("decrease".equals(action) && currentQuantity > 1) {
                currentQuantity -= 1;
            }

            cart.put(productId, currentQuantity);
        }
        redirectAttributes.addFlashAttribute("message","Cập nhật giỏ hàng thành công");

        // Lưu giỏ hàng vào session
        session.setAttribute("cart", cart);

        return "redirect:/home/cart";
    }


    @GetMapping("/cart/remove/{productId}")
    public String removeFromCart(@PathVariable("productId") Long productId, HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        // Lấy giỏ hàng từ session
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
        }

        if (cart.containsKey(productId)) {
            cart.remove(productId);
        }
        redirectAttributes.addFlashAttribute("message","Bạn đã xóa sản phẩm thành công");

        session.setAttribute("cart", cart);

        return "redirect:/home/cart";
    }



}

