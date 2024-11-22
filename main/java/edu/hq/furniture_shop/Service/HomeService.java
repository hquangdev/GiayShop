package edu.hq.furniture_shop.Service;

import edu.hq.furniture_shop.Model.Product;
import edu.hq.furniture_shop.Model.Category;
import edu.hq.furniture_shop.Model.Slide;
import edu.hq.furniture_shop.Repository.CategoryRepository;
import edu.hq.furniture_shop.Repository.ProductRepository;
import edu.hq.furniture_shop.Repository.SlideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HomeService {

    @Autowired
    private SlideRepository slideRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private ProductRepository productRepo;

    public List<Slide> listAllSlide(){
        return slideRepo.findAll();
    }

    public List<Category> getAllCategory(){
        return categoryRepo.findAll();
    }

    public List<Product> getAllProduct(){
        return productRepo.findAll();
    }



}
