package edu.hq.furniture_shop.Service;

import edu.hq.furniture_shop.Model.Category;
import edu.hq.furniture_shop.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepo;

    public void deleteCategory(Long id) {
        categoryRepo.deleteById(id);
    }

    public Optional<Category> editCategory(Long id) {
        return categoryRepo.findById(id);
    }

    public void addCategory(Category category){
        Optional<Category> existingCategory = categoryRepo.findByName(category.getName());
        if (existingCategory.isPresent()) {
            throw new IllegalArgumentException("Tên danh mục đã tồn tại!");
        }
        categoryRepo.save(category);
    }

    //câp nhật danh muc
    public void updateCategory(Long id, Category updatedCategory) {
        // Tìm danh mục theo ID
        Category existingCategory = categoryRepo.findById((id))
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy danh mục với ID: " + id));

        // Kiểm tra xem tên danh mục mới có bị trùng không (trừ chính danh mục hiện tại)
        Optional<Category> categoryWithSameName = categoryRepo.findByName(updatedCategory.getName());
        if (categoryWithSameName.isPresent() && !categoryWithSameName.get().getId().equals(id)) {
            throw new IllegalArgumentException("Tên danh mục đã tồn tại!");
        }

        // Cập nhật thông tin
        existingCategory.setName(updatedCategory.getName());
        existingCategory.setContent(updatedCategory.getContent());

        categoryRepo.save(existingCategory);
    }
}