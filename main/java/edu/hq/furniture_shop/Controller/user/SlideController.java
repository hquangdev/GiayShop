package edu.hq.furniture_shop.Controller.user;

import edu.hq.furniture_shop.Model.Slide;
import edu.hq.furniture_shop.Repository.SlideRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RequestMapping("/admin/slide")
@Controller
public class SlideController {

    @Autowired
    private SlideRepository slideRepository;

    // Thư mục lưu ảnh
    private final String uploadDir = "F:/File_web/Furniture_shop/src/main/resources/static/client/assets/images/slider/";

    @GetMapping("/list")
    public String list(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        int role = (int) session.getAttribute("role");

        if (role == 0) {
            return "redirect:/home/login";
        }

        List<Slide> listSlide = slideRepository.findAll();
        model.addAttribute("slide", listSlide);
        model.addAttribute("contentAdmin", "/admin/slide/list");
        model.addAttribute("role", role);
        model.addAttribute("username", username);
        return "/layouts/admin";
    }

    // Hiển thị form thêm slide
    @GetMapping("/add")
    public String showAddForm(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");

        if (username == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("slide", new Slide());
        model.addAttribute("contentAdmin", "admin/slide/add");
        return "layouts/admin";
    }

    // Xử lý thêm slide
    @PostMapping("/add-slide")
    public String addSlide(@RequestParam("img") MultipartFile file,
                           @RequestParam("title") String title,
                           @RequestParam("content") String content) {
        // Khởi tạo đối tượng slide
        Slide slide = new Slide();

        if (!file.isEmpty()) {
            try {
                String fileName = file.getOriginalFilename();

                // Tạo đường dẫn file lưu trữ
                File uploadFile = new File(uploadDir + fileName);

                // Lưu file
                file.transferTo(uploadFile);

                slide.setImg(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                return "redirect:/admin/slide/add?error=file_upload";
            }
        } else {
            return "redirect:/admin/slide/add?error=empty_file";
        }

        slide.setTitle(title);
        slide.setContent(content);

        // Lưu vào cơ sở dữ liệu
        slideRepository.save(slide);

        // Điều hướng tới danh sách slide
        return "redirect:/admin/slide/list";
    }


    // Hiển thị form sửa slide
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");

        if (username == null) {
            return "redirect:/admin/login";
        }

        Optional<Slide> slide = slideRepository.findById(id);
        if (slide.isPresent()) {
            model.addAttribute("slide", slide.get());
            model.addAttribute("contentAdmin", "admin/slide/edit");
            return "layouts/admin";
        }
        return "redirect:/admin/slide/list";
    }

    // Xử lý cập nhật slide
    @PostMapping("/update")
    public String updateSlide(@RequestParam("id") int id, @ModelAttribute Slide slide, @RequestParam("image") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                // Lưu file mới vào thư mục
                String fileName = file.getOriginalFilename();
                File uploadFile = new File(uploadDir + fileName);
                file.transferTo(uploadFile);

                slide.setImg(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        slide.setId(id);
        slideRepository.save(slide);
        return "redirect:/admin/slide/list";
    }

    // Xóa slide
    @GetMapping("/delete/{id}")
    public String deleteSlide(@PathVariable int id) {
        slideRepository.deleteById(id);
        return "redirect:/admin/slide/list";
    }
}
