package edu.hq.furniture_shop.Controller.admin;

import edu.hq.furniture_shop.Model.Category;
import edu.hq.furniture_shop.Model.Product;
import edu.hq.furniture_shop.Repository.CategoryRepository;
import edu.hq.furniture_shop.Repository.ProductRepository;
import edu.hq.furniture_shop.Service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.lang.model.element.Name;
import java.io.IOException;
import java.util.List;
import java.util.Timer;


@RequestMapping(value = "/admin/products")
@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired private ProductService productSV;

  @Autowired
  private CategoryRepository categoryRepository;

    @GetMapping("/list")
    public String product(Model model, HttpSession session){
        String username = (String) session.getAttribute("username");
        int role = (int) session.getAttribute("role");

        if (role == 0) {
            return "redirect:/home/login";
        }

        model.addAttribute("contentAdmin", "admin/products/list");
        model.addAttribute("role", role);
        model.addAttribute("username", username);
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("category", categoryRepository.findAll());
        return"layouts/admin";
    }

    //chuyẻn tới trang thêm ssản phẩm
    @GetMapping("/add")
    public String AddProduct(Model model, HttpSession session){

        String username = (String) session.getAttribute("username");

        if (username == null) {
            return "redirect:/home/login";
        }

        model.addAttribute("contentAdmin", "admin/products/add");
        model.addAttribute("category", categoryRepository.findAll());
        return "layouts/admin";

    }


    @PostMapping("/add-product")
    public String addProduct(@RequestParam("name") String name,
                             @RequestParam("title") String title,
                             @RequestParam("description") String description,
                             @RequestParam("price") int price,
                             @RequestParam("quantity") int quantity,
                             @RequestParam("image") MultipartFile file,
                             @RequestParam("category_Id") Long categoryId) throws IOException {

        // Lấy danh mục từ database
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Danh mục không hợp lệ"));

        productSV.addProduct(name, title, description, price, quantity, file, category);

        return "redirect:/admin/products/list";
    }

    // Hiển thị form sửa sản phẩm
    @GetMapping("/edit/{id}")
    public String showEditProductForm(@PathVariable("id") Long id, Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");

        if (username == null) {
            return "redirect:/home/login";
        }

        Product product = productSV.getProductById(id);
        model.addAttribute("contentAdmin", "admin/products/edit");
        model.addAttribute("category", categoryRepository.findAll());
        model.addAttribute("product", product);
        return "layouts/admin";
    }

    @PostMapping("/update")
    public String updateProduct(@RequestParam("id") Long id,
                                @RequestParam("name") String name,
                                @RequestParam("title") String title,
                                @RequestParam("description") String description,
                                @RequestParam("price") int price,
                                @RequestParam("quantity") int quantity,
                                @RequestParam("category_Id") Long categoryId,
                                @RequestParam("image") MultipartFile file) throws IOException {

        // Tìm category theo ID
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Danh mục không hợp lệ"));

        // Gọi service cập nhật sản phẩm
        productSV.updateProduct(id, name, title, description, price, quantity, category, file);

        // Sau khi cập nhật, chuyển hướng về trang danh sách sản phẩm
        return "redirect:/admin/products/list";
    }


    // Xóa sản phẩm
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id, HttpSession session) {
        productSV.deleteProduct(id);

        return "redirect:/admin/products/list";
    }


}

