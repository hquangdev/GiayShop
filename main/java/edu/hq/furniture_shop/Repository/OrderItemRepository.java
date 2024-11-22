package edu.hq.furniture_shop.Repository;

import edu.hq.furniture_shop.Model.Order;
import edu.hq.furniture_shop.Model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrder(Order order);
}
