package edu.hq.furniture_shop.Repository;

import edu.hq.furniture_shop.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByName(String name);

    List<Product> findByNameContainingIgnoreCase(String query);
}
