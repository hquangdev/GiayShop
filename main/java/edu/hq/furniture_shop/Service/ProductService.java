package edu.hq.furniture_shop.Service;

import edu.hq.furniture_shop.Model.Category;
import edu.hq.furniture_shop.Model.Product;
import edu.hq.furniture_shop.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Đường dẫn lưu ảnh
    private final String upImage = "F:/File_web/Furniture_shop/src/main/resources/static/client/assets/images/products/";

    // Thêm sản phẩm mới
    public void addProduct(String name, String title, String description, int price, int quantity,
                           MultipartFile file, Category category) throws IOException {
        // Tạo đối tượng Product mới
        Product product = new Product();
        product.setName(name);
        product.setTitle(title);
        product.setDescription(description);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setCategory(category);

        productRepository.save(product);

        // Kiểm tra nếu có ảnh được tải lên
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            String filePath = upImage + fileName;

            File destinationFile = new File(filePath);
            file.transferTo(destinationFile);

            product.setImage(fileName);

            productRepository.save(product);
        }
    }


    // Cập nhật sản phẩm và ảnh
    public void updateProduct(Long id, String name, String title, String description,
                              int price, int quantity, Category category, MultipartFile file) throws IOException {
        // Tìm sản phẩm trong cơ sở dữ liệu
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sản phẩm không tồn tại"));

        // Cập nhật thông tin sản phẩm
        existingProduct.setName(name);
        existingProduct.setTitle(title);
        existingProduct.setDescription(description);
        existingProduct.setPrice(price);
        existingProduct.setQuantity(quantity);
        existingProduct.setCategory(category);

        // Kiểm tra nếu có ảnh mới được tải lên
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            String filePath = upImage + fileName;

            // Lưu ảnh vào thư mục
            File destinationFile = new File(filePath);
            file.transferTo(destinationFile);

            // Cập nhật tên ảnh vào sản phẩm
            existingProduct.setImage(fileName);
        }

        // Lưu lại sản phẩm sau khi cập nhật
        productRepository.save(existingProduct);
    }



    // Xóa sản phẩm và ảnh liên quan
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        // Xóa ảnh liên quan (chỉ có một ảnh duy nhất)
        String imageName = product.getImage();
        if (imageName != null && !imageName.isEmpty()) {
            String filePath = upImage + imageName;  // Đường dẫn ảnh
            File file = new File(filePath);  // Tạo đối tượng file
            if (file.exists()) {
                file.delete();  // Xóa file ảnh nếu tồn tại
            }
        }

        // Xóa sản phẩm
        productRepository.delete(product);
    }


    // Lấy sản phẩm theo id
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
    }
}



