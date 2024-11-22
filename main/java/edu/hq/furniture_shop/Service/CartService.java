package edu.hq.furniture_shop.Service;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CartService {
    private static final String CART_SESSION_KEY = "cart";

    public void addToCart(HttpSession session, Long productId, int quantity) {
        // Lấy giỏ hàng từ session, nếu chưa có thì tạo mới
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute(CART_SESSION_KEY);
        if (cart == null) {
            cart = new HashMap<>();
        }

        // Thêm sản phẩm vào giỏ hàng
        cart.put(productId, cart.getOrDefault(productId, 0) + quantity);
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    public Map<Long, Integer> getCart(HttpSession session) {
        return (Map<Long, Integer>) session.getAttribute(CART_SESSION_KEY);
    }

    public void clearCart(HttpSession session) {
        session.removeAttribute(CART_SESSION_KEY);
    }
}
