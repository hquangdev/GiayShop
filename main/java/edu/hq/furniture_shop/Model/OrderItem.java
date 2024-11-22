package edu.hq.furniture_shop.Model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "orderItem")
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_order_id")
    private Product product;

    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

}

