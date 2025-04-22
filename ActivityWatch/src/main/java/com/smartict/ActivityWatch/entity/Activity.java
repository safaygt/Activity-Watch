package com.smartict.ActivityWatch.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="activity")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column
    private TypeEnum type;




}
