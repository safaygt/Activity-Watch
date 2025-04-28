package com.smartict.activitywatch.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "usr")
@Data
public class Usr {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String username;




}
