package com.smartict.ActivityWatch.entity;


import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "usrActivity")
public class UsrActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "fkusrId", nullable = false)
    private Usr usr;

    @ManyToOne
    @JoinColumn(name = "fkactivityId", nullable = false)
    private Activity activity;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime date;


    @Column
    private boolean isAfk;






}
