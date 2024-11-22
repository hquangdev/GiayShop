package edu.hq.furniture_shop.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name="client")
@Getter
@Setter
@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String username;
    private String password;
}
