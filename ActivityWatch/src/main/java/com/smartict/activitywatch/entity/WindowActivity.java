package com.smartict.activitywatch.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="WindowActivity")
public class WindowActivity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String windowTitle;
}
