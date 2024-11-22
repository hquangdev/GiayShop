package edu.hq.furniture_shop.Controller.user;

import edu.hq.furniture_shop.Model.Product;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class HomeController extends BaseController {

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
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
    public String getProductDetails(@PathVariable Long id, Model model) {
        // Lấy thông tin sản phẩm dựa vào ID
        Product product = productSv.getProductById(id);


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
