package edu.hq.furniture_shop.Controller.user;

import edu.hq.furniture_shop.Model.Product;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Controller
public class HomeController extends BaseController {

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {

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

        model.addAttribute("cart", cartProducts);
        model.addAttribute("content", "Client/home");
        model.addAttribute("product", homeSV.getAllProduct());
        model.addAttribute("slides", homeSV.listAllSlide());
        model.addAttribute("categories", homeSV.getAllCategory());

        return "/layouts/client";
    }

    @GetMapping("/home/login")
    public String login(Model model){
        model.addAttribute("content", "Client/login");
        return "/layouts/client";
    }

    @GetMapping("/home/productDetails/{id}")
    public String getProductDetails(@PathVariable Long id, Model model, HttpSession session) {

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
        // Lấy thông tin sản phẩm dựa vào ID
        Product product = productSv.getProductById(id);

        model.addAttribute("cart", cartProducts);
        model.addAttribute("category", homeSV.getAllCategory());
        model.addAttribute("product", product);
        model.addAttribute("content", "Client/productDetail");
        return "/layouts/client";
    }


    //tìm kiếm
    @GetMapping("/home/search")
    public String searchProducts(@RequestParam("query") String query, Model model) {
        // Tìm kiếm sản phẩm theo từ khóa 'query'
        List<Product> products = productRepository.findByNameContainingIgnoreCase(query);

        model.addAttribute("product", products);

        model.addAttribute("slides", homeSV.listAllSlide());
        model.addAttribute("categories", homeSV.getAllCategory());
        model.addAttribute("content", "Client/home");
        return "/layouts/client";
    }



}
