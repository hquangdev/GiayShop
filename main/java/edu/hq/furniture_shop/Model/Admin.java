package edu.hq.furniture_shop.Model;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "admins")
@Entity
public class Admin{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private  String email;
    private String password;
    private String address;
    private String phone;

}
