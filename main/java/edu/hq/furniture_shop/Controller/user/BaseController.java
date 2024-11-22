package edu.hq.furniture_shop.Controller.user;


import edu.hq.furniture_shop.Repository.ClientRepository;

//import edu.hq.furniture_shop.Service.ClientService;
import edu.hq.furniture_shop.Repository.ProductRepository;
import edu.hq.furniture_shop.Service.HomeService;
import edu.hq.furniture_shop.Service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class BaseController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductService productSv;

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    HomeService homeSV;

}
