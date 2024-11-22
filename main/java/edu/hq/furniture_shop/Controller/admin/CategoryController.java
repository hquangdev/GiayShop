package edu.hq.furniture_shop.Controller.admin;

import edu.hq.furniture_shop.Model.Category;
import edu.hq.furniture_shop.Repository.CategoryRepository;
import edu.hq.furniture_shop.Service.CategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Optional;

@RequestMapping(value="/admin/categorys")
@Controller
public class CategoryController  {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categorySV;

    @GetMapping("/list")
    public String listCategory(Model model, HttpSession session){

        String username = (String) session.getAttribute("username");
        Integer role = (Integer) session.getAttribute("role");

        // Kiểm tra xem username hoặc role có null không
        if (username == null || role == null) {
            return "redirect:/home/login";
        }

        model.addAttribute("contentAdmin", "admin/categorys/list");

        model.addAttribute("role", role);
        model.addAttribute("username", username);

        model.addAttribute("category", categoryRepository.findAll());
        return"layouts/admin";
    }

    @GetMapping("/add")
    public String showAddCategory(Model model, HttpSession session){

        String username = (String) session.getAttribute("username");

        if (username == null) {
            return "redirect:/home/login";
        }

        model.addAttribute("contentAdmin", "admin/categorys/add");
        return"layouts/admin";
    }

    @PostMapping("/add-category")
    public String addCategory(@ModelAttribute Category category, Model model){
        categorySV.addCategory(category);

        model.addAttribute("contentAdmin", "admin/categorys/add");
        return"layouts/admin";
    }

    @GetMapping("/edit/{id}")
    public String editCategoryPage(@PathVariable Long id, Model model, HttpSession session) {

        String username = (String) session.getAttribute("username");

        if (username == null) {
            return "redirect:/home/login";
        }

        // Tìm danh mục theo ID
        Category category = categoryRepository.findById((id))
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy danh mục với ID: " + id));

        // Đưa danh mục tìm được vào Model để hiển thị trên giao diện
        model.addAttribute("category", category);
        model.addAttribute("contentAdmin", "admin/categorys/edit");
        return "layouts/admin";
    }

    // cập nhật
    @PostMapping("/edit-category")
    public String updateCategory(@RequestParam Long id, @ModelAttribute Category category, Model model) {
            category.setId(id);
            categorySV.updateCategory(id, category);

            model.addAttribute("contentAdmin", "admin/categorys/edit");
        return "redirect:/admin/categorys/list";
    }



    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, Model model) {
            categorySV.deleteCategory((id));
            model.addAttribute("contentAdmin", "admin/categorys/list");
        return "redirect:/admin/categorys/list";
    }



}
